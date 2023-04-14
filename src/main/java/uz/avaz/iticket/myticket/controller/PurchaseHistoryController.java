package uz.avaz.iticket.myticket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.avaz.iticket.myticket.service.PurchaseHistoryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseHistoryController {

    @Autowired
    PurchaseHistoryService purchaseService;


    @GetMapping
    public ResponseEntity<?> purchaseTicket(){
        return purchaseService.purchaseTicket();
    }

    @GetMapping("/refund/{id}")
    public ResponseEntity<?> refundTicket(@PathVariable UUID id,@RequestParam(value = "isRefund",defaultValue = "false") Boolean isRefund){
        return purchaseService.refundTicket(id,isRefund);
    }
}
