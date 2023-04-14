package uz.avaz.iticket.myticket.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.avaz.iticket.myticket.model.Genre;

import java.util.UUID;

@Projection(types = {Genre.class})
public interface GenreProjection {

    UUID getId();

    String getName();

}
