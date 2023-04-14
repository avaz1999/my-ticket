package uz.avaz.iticket.myticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.avaz.iticket.myticket.enums.TicketStatus;
import uz.avaz.iticket.myticket.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "tickets")
public class Ticket extends AbsEntity {

    private String qrCode;

    private double price;

    @ManyToOne
    private MovieSession movieSession;

    @ManyToOne
    private Seat seat;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

}
