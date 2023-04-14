package uz.avaz.iticket.myticket.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.avaz.iticket.myticket.model.Seat;

import java.util.UUID;

@Projection(types = {Seat.class})
public interface AvailableSeatProjection {

    UUID getId();

    String getSeatNumber();

    String getRowNumber();

    Boolean getAvailable();
}
