package uz.avaz.iticket.myticket.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.avaz.iticket.myticket.model.Ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Projection(types = {Ticket.class})
public interface TicketProjection {

    UUID getId();

    Double getPrice();

    String getHallName();

    Integer getRowNumber();

    Integer getSeatNumber();

    LocalDate getStartDate();

    LocalTime getStartTime();

    String getMovieTitle();
}
