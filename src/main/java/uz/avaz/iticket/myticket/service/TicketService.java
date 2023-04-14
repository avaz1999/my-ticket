package uz.avaz.iticket.myticket.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.avaz.iticket.myticket.dto.TicketDto;
import uz.avaz.iticket.myticket.enums.TicketStatus;
import uz.avaz.iticket.myticket.model.MovieSession;
import uz.avaz.iticket.myticket.model.Seat;
import uz.avaz.iticket.myticket.model.Ticket;
import uz.avaz.iticket.myticket.model.User;
import uz.avaz.iticket.myticket.payload.ApiResponse;
import uz.avaz.iticket.myticket.projection.TicketProjection;
import uz.avaz.iticket.myticket.repository.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static uz.avaz.iticket.myticket.utils.Constants.*;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepo;

    @Autowired
    MovieSessionRepository movieSessionRepo;

    @Autowired
    SeatRepository seatRepo;

    @Autowired
    PurchaseWaitingTimeRepository timeRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> generateTicket(TicketDto ticketDto, HttpServletResponse response) {
        try {

            UUID seatId = ticketDto.getSeatId();
            UUID sessionId = ticketDto.getSessionId();

            MovieSession movieSession = movieSessionRepo.findById(sessionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie session not found!"));

            Seat seat = seatRepo.findById(seatId)
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found!"));

            Boolean existsTicket = ticketRepo.existsTicket(seat.getId(), movieSession.getId());

            if (existsTicket) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(EXISTS,false,null));
            }else{

                Double ticketPrice = ticketRepo.calculateTicketPrice(seat.getId(), movieSession.getId());


                // TODO: 3/28/2022 USER VA QR_CODE NI BERIB YUBORISH KERAK
                User user = userRepository.findByUsername("aziz_05").orElseThrow(() -> new ResourceNotFoundException("User not found!"));

                Ticket ticket = new Ticket(
                        null,
                        ticketPrice,
                        movieSession,
                        seat,
                        user,
                        TicketStatus.NEW
                );

                Ticket ticket1 = ticketRepo.save(ticket);
                String ticketId= String.valueOf(ticket1.getId());
//                Cookie cookie = new Cookie("ticketId",ticketId);
//                cookie.setMaxAge(86400);
//                cookie.setPath("/");
//                cookie.setDomain("localhost");
//                response.addCookie(cookie);
                Timer timer = new Timer();
                TimerTask deleteTicket = new TimerTask() {
                    @Override
                    public void run() {
                        Ticket savedTicket = ticketRepo.findById(ticket1.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found!"));

                        if (savedTicket.getTicketStatus().equals(TicketStatus.NEW)) {
                            ticketRepo.delete(savedTicket);
                            System.out.println("TICKET DELETED");
                        }
                    }
                };
                Integer waitingTime = timeRepository.findFirstByUpdatedAt();
                if (waitingTime != null)
                timer.schedule(deleteTicket, waitingTime*60*1000);
                timer.schedule(deleteTicket, 30*60*1000);

                return ResponseEntity.ok(new ApiResponse(SUCCESS_SAVE,true,ticket1));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(OBJECT_NOT_FOUND,false,null));
        }
    }

    public ResponseEntity<?> deleteTicket(UUID id) {
        if (id == null || id.equals("")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(ERROR,false,null));
        try {
            Ticket ticket = ticketRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found!"));
            ticketRepo.delete(ticket);
            return ResponseEntity.ok(new ApiResponse(SUCCESS_DELETE,true,null));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(FAILED_TO_DELETE,false,null));
        }
    }

    public ResponseEntity<?> getAllTickets() {
        try{
            List<TicketProjection> ticketsAll = ticketRepo.findTicketsAll();
            return ResponseEntity.ok(new ApiResponse(SUCCESS,true,ticketsAll));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(EMPTY_LIST,false,true));
        }
    }


    public ResponseEntity<?> getMYTicket() {
        try {
        User user = userRepository.findByUsername("aziz_05").orElseThrow(() -> new ResourceNotFoundException("User not found!"));
            List<TicketProjection> tickets = ticketRepo.getTicketsByUser(user.getId());
            return ResponseEntity.ok(new ApiResponse(SUCCESS,true,tickets));

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(ERROR,false,null));
        }


    }
}
