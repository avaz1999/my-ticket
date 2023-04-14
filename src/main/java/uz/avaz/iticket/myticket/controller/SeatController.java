package uz.avaz.iticket.myticket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.avaz.iticket.myticket.dto.SeatDataDto;
import uz.avaz.iticket.myticket.dto.SeatDto;
import uz.avaz.iticket.myticket.service.SeatService;

import java.util.UUID;

@RestController
@RequestMapping("/api/seat")
public class SeatController {

    @Autowired
    SeatService seatService;

    @GetMapping
    public ResponseEntity<?> getAllSeat(@RequestParam(value = "rowId")UUID rowId){
        return seatService.getAllSeat(rowId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSeatById(@PathVariable UUID id){
        return seatService.getSeatById(id);
    }

    @PostMapping
    public ResponseEntity<?> addSeat(@RequestParam("rowId") UUID rowId, @RequestBody SeatDto seatDto){
        return seatService.addSeat(rowId,seatDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editSeat(@PathVariable UUID id, @RequestBody SeatDataDto seatDataDto){
        return seatService.editData(id,seatDataDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSeat(@PathVariable UUID id){
        return seatService.deleteSeat(id);
    }
}
