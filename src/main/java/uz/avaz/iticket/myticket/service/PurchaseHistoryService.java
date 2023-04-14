package uz.avaz.iticket.myticket.service;


import com.stripe.Stripe;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.avaz.iticket.myticket.enums.PurchaseStatus;
import uz.avaz.iticket.myticket.enums.TicketStatus;
import uz.avaz.iticket.myticket.model.*;
import uz.avaz.iticket.myticket.payload.ApiResponse;
import uz.avaz.iticket.myticket.projection.TicketProjection;
import uz.avaz.iticket.myticket.repository.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uz.avaz.iticket.myticket.utils.Constants.*;

@Service
public class PurchaseHistoryService {


    @Value("${STRIPE_SECRET_KEY}")
    String stripeApiKey;

    @Value("${BASE_URL}")
    String baseUrl;

    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    PayTypeRepository payTypeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CashBoxRepository cashBoxRepository;

    @Autowired
    RefundChargeFeeRepository refundRepository;


    @Transactional
    public ResponseEntity<?> purchaseTicket() {
        try {

            User user = userRepository.findByUsername("aziz_05").orElseThrow(() -> new ResourceNotFoundException("User not found!"));

            List<TicketProjection> ticketList = ticketRepository.getTicketsByUser(user.getId());

            String successURL = baseUrl + "payment/success";
            String failureURL = baseUrl + "payment/failed";

            Stripe.apiKey = stripeApiKey;

            List<SessionCreateParams.LineItem> sessionItemList = new ArrayList<>();
            for (TicketProjection ticket : ticketList) {
                sessionItemList.add(createSessionLineItem(ticket));
            }
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setClientReferenceId(user.getId().toString())
                    .setCancelUrl(failureURL)
                    .setSuccessUrl(successURL)
                    .addAllLineItem(sessionItemList)
                    .build();

            Session session = Session.create(params);

            return ResponseEntity.ok(new ApiResponse(SUCCESS, true, session.getUrl()));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
        }
    }

    private SessionCreateParams.LineItem createSessionLineItem(TicketProjection ticket) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(ticket))
                .setQuantity(1L)
                .build();
    }


    private SessionCreateParams.LineItem.PriceData createPriceData(TicketProjection ticket) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("USD")
                .setUnitAmount((long) ((ticket.getPrice() * 100 + 30) / 0.971))
                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                        .builder()
                        .setName(" | " + ticket.getMovieTitle() + " -> " + ticket.getHallName() + " : " + ticket.getStartDate() + " " + ticket.getStartTime() + " | ")
                        .build())
                .build();
    }


    public ResponseEntity<?> fulfillOrder(Session session) {
        String clientReferenceId = session.getClientReferenceId();
        String paymentIntent = session.getPaymentIntent();
        try {
            Double totalPrice = Double.valueOf(0);
            User user = userRepository.findById(UUID.fromString(clientReferenceId)).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
            PayType payType = payTypeRepository.findPayTypeByName("Stripe-Card");
            List<Ticket> ticketsByUserId = ticketRepository.getTicketsByUserId(user.getId());
            if (payType != null) {
                for (Ticket ticket : ticketsByUserId) {
                    if (ticket.getTicketStatus().equals(TicketStatus.NEW)) {
                        totalPrice += ticket.getPrice();
                        ticket.setTicketStatus(TicketStatus.PURCHASED);
                        ticket.setQrCode("http://localhost:8080/api/check-ticket/"+ticket.getId());
                        Ticket saveTicket = ticketRepository.save(ticket);
                    }
                }
                purchaseRepository.save(new PurchaseHistory(ticketsByUserId,totalPrice, payType, paymentIntent, PurchaseStatus.PURCHASE_PAID));
                CashBox cashBox = cashBoxRepository.findAll().get(0);
                cashBox.setBalance(cashBox.getBalance() + totalPrice);
                cashBoxRepository.save(cashBox);
                return ResponseEntity.ok(new ApiResponse(SUCCESS, true, null));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(ERROR,false,null));
            }


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
        }
    }

    public ResponseEntity<?> refundTicket(UUID id, Boolean isRefund) {
        if (id == null || id.equals(""))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR, false, null));
        try {
            Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found!"));
            LocalDate startDate = ticket.getMovieSession().getStartDate();
            LocalTime startTime = ticket.getMovieSession().getStartTime();
            LocalDateTime localDateTime = startDate.atTime(startTime);
            long between = ChronoUnit.MINUTES.between(LocalDateTime.now(), localDateTime);
            Integer waitingTime = 3600;
            List<Ticket> tickets = new ArrayList<>();
            tickets.add(ticket);
            PurchaseHistory purchaseHistory = purchaseRepository.findPurchaseHistoryByTickets(ticket.getId());
            if (between < 0) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("This session already is over!", false, null));
            } else if (between <= waitingTime) {
                if (isRefund) {
                    ticket.setTicketStatus(TicketStatus.REFUNDED);
                    ticketRepository.save(ticket);
                    purchaseRepository.save(new PurchaseHistory(tickets,0.0,purchaseHistory.getPayType(),null,PurchaseStatus.PURCHASE_REFUNDED));
                    tickets.clear();
                    return ResponseEntity.ok(new ApiResponse(SUCCESS_REFUND,true,null));
                } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("You can return your ticket but your money will not be refunded because the movie is an hour away from the start.", false, null));
                }
            } else if (between > waitingTime) {

                Integer interval = Math.toIntExact(between);
                double refundPercent = refundRepository.getPercent(interval) != null ? refundRepository.getPercent(interval) : 0;
                double additionalFee = ticket.getPrice() * refundPercent / 1000;
                double refundedTicketPrice = ticket.getPrice() - additionalFee;
                if (!isRefund) {
                    return ResponseEntity.ok(new ApiResponse("You will be charged a " + refundPercent + "% commission and you will be refunded $ " + refundedTicketPrice + " for returning your ticket on " + LocalDate.now() + " " + LocalTime.now() + ". Are you sure you want to return your ticket?", true, null));
                } else {

                    if (purchaseHistory.getPayType().getName().equals("Stripe-Card")){
                    Stripe.apiKey = stripeApiKey;
                    RefundCreateParams params = RefundCreateParams
                            .builder()
                            .setPaymentIntent(purchaseHistory.getPaymentIntentId())
                            .setAmount((long) refundedTicketPrice*100)
                            .build();
                    Refund refund = Refund.create(params);
                    if (refund.getStatus().equals("succeeded")) {
                        CashBox cashBox = cashBoxRepository.findAll().get(0);
                        cashBox.setBalance(cashBox.getBalance() - refundedTicketPrice);
                        ticket.setTicketStatus(TicketStatus.REFUNDED);
                        Ticket saveTicket = ticketRepository.save(ticket);
                        cashBoxRepository.save(cashBox);
                        purchaseRepository.save(new PurchaseHistory(tickets,refundedTicketPrice,purchaseHistory.getPayType(),null,PurchaseStatus.PURCHASE_REFUNDED));
                        tickets.clear();
                    return ResponseEntity.ok(new ApiResponse("You were charged a " + refundPercent + "% commission for returning your ticket on " + LocalDate.now() + " " + LocalTime.now() + " and you were refunded $ " + refundedTicketPrice + ". Ticket return was successful.", true, null));
                    }else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(ERROR,false,null));
                    }

                    }else {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_REFUND,false,null));
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(ERROR, false, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND, false, null));
        }
    }
}
