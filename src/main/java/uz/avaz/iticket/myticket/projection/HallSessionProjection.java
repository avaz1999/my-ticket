package uz.avaz.iticket.myticket.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uz.avaz.iticket.myticket.model.Hall;

import java.util.List;
import java.util.UUID;

@Projection(types = {Hall.class})
public interface HallSessionProjection {

    UUID getId();

//    UUID getMovieAnnouncementId();

    String getHallName();

//    LocalDate getStartDate();

    @Value("#{@movieSessionRepository.getHallTime(target.id,target.movieAnnouncementId,target.startDate)}")
    List<TimeSessionProjection> getTimes();
}
