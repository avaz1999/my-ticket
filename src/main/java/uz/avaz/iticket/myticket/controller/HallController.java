package uz.avaz.iticket.myticket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.avaz.iticket.myticket.dto.HallDto;
import uz.avaz.iticket.myticket.service.HallService;

import java.util.UUID;

@RestController
@RequestMapping("/api/hall")
public class HallController {

    @Autowired
    HallService hallService;

    @GetMapping
    public ResponseEntity<?> getAllHalls(){
            return hallService.getAllHalls();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHallById(@PathVariable UUID id){
        return hallService.getHallById(id);
    }

    @PostMapping
    public ResponseEntity<?> addHall(@RequestBody HallDto hallDto){
        return hallService.addHall(hallDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editHall(@PathVariable UUID id,@RequestBody HallDto hallDto){
        return hallService.editHall(id,hallDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHall(@PathVariable UUID id){
       return hallService.deleteHall(id);

    }
}
