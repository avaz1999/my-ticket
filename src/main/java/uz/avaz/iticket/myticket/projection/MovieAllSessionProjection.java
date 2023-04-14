package uz.avaz.iticket.myticket.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uz.avaz.iticket.myticket.model.MovieSession;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Projection(types = {MovieSession.class},name = "allMovieSessionProjection")
public interface MovieAllSessionProjection {
//    UUID getId();

    UUID getMovieAnnouncementId();

    UUID getMovieId();

    UUID getMoviePosterImageId();

    String getMovieTitle();

    LocalDate getStartDate();

    @Value("#{@movieSessionRepository.getSessionHall(target.movieAnnouncementId,target.startDate)}")
    List<HallSessionProjection> getHalls();
}
