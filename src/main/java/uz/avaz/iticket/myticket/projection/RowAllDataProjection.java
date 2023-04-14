package uz.avaz.iticket.myticket.projection;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.UUID;

public interface RowAllDataProjection {

    UUID getId();

    Integer getNumber();

    @Value("#{@seatRepository.findSeatsByRowId(target.id)}")
    List<SeatAllDataProjection> getSeats() ;
}
