package uz.avaz.iticket.myticket.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.avaz.iticket.myticket.dto.TicketDto;
import uz.avaz.iticket.myticket.service.TicketService;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @GetMapping
    public ResponseEntity<?> getAllTickets(){
        return ticketService.getAllTickets();
    }

    @GetMapping("/get-my-ticket")
    public ResponseEntity<?> getMyTicket(){
        return ticketService.getMYTicket();
    }

    @PostMapping
    public ResponseEntity<?> generateTicket(@RequestBody TicketDto ticketDto, HttpServletResponse response){
        return ticketService.generateTicket(ticketDto,response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable UUID id){
        return ticketService.deleteTicket(id);
    }

}
