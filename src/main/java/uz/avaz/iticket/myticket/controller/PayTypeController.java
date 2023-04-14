package uz.avaz.iticket.myticket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.avaz.iticket.myticket.service.PayTypeService;

import java.util.UUID;

@RestController
@RequestMapping("/api/pay-type")
public class PayTypeController {

    @Autowired
    PayTypeService payTypeService;

    @GetMapping
    public ResponseEntity<?> getAllPayType(){
        return payTypeService.getAllPayType();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPayTypeById(@PathVariable UUID id){
        return payTypeService.getPayTypeById(id);
    }

    @PostMapping
    public ResponseEntity<?> addPayType(@RequestPart("file")  MultipartFile file, @RequestPart("name") String name){
        return payTypeService.addPayType(file,name);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayType(@PathVariable UUID id){
        return payTypeService.deletePayType(id);
    }
}
