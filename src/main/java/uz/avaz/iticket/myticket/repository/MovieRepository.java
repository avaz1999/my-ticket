package uz.avaz.iticket.myticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.avaz.iticket.myticket.model.Movie;
import uz.avaz.iticket.myticket.projection.MovieProjection;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {

    @Query(nativeQuery = true,value = "select cast(m.id as varchar) as id, " +
            "cast(a.id as varchar) as posterImageId," +
            "m.title as title," +
            "m.description as description," +
            "m.duration_in_min as durationInMin," +
            "m.trailer_video_url as trailerVideoUrl," +
            "m.release_date as releaseDate," +
            "m.budget as budget" +
            "  from movies m join attachments a on a.id = m.poster_img_id where m.id = :id")
    MovieProjection findMovieById(UUID id);
}
