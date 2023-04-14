package uz.avaz.iticket.myticket.controller;

import com.stripe.Stripe;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.avaz.iticket.myticket.payload.ApiResponse;
import uz.avaz.iticket.myticket.service.PurchaseHistoryService;

import java.util.Objects;

import static uz.avaz.iticket.myticket.utils.Constants.ERROR;

@RestController
public class StripePaymentController {

    @Autowired
    PurchaseHistoryService purchaseHistoryService;

    @Value("${STRIPE_SECRET_KEY}")
    String stripeApiKey;

    @Value("${BASE_URL}")
    String baseUrl;

    String endpointSecret="whsec_e4aa9b52a2ac96400f3825ae99dbd3b73e0f730d565ed9960dd00ee23518f3c5";

    @RequestMapping(value = "payment/success", method = RequestMethod.GET)
    public ResponseEntity<?> paymentSuccess(){
        return ResponseEntity.ok().body("Payment successes");
    }

    @RequestMapping(value = "payment/failed", method = RequestMethod.GET)
    public ResponseEntity<?> paymentFailed(){
        return ResponseEntity.ok().body("Payment failed");
    }

    @RequestMapping( value = "stripe-webhook",method = RequestMethod.POST)
    public ResponseEntity<?> handle(@RequestBody String payload, @RequestHeader(name = "Stripe-Signature") String sigHeader) {
    Stripe.apiKey=stripeApiKey;

        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (Exception e){
            e.printStackTrace();
        }

        if ("checkout.session.completed".equals(Objects.requireNonNull(event).getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().get();
          return purchaseHistoryService.fulfillOrder(session);
        }
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(ERROR,false,null));
    }

}
