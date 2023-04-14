package uz.avaz.iticket.myticket.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.avaz.iticket.myticket.model.Cast;

import java.util.UUID;

@Projection(types = {Cast.class})
public interface CastProjection {

    UUID getId();

    String getFullName();

    String getCastType();
}
