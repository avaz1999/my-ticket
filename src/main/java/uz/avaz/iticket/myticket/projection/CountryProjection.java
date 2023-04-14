package uz.avaz.iticket.myticket.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.avaz.iticket.myticket.model.Country;

import java.util.UUID;

@Projection(types = {Country.class})
public interface CountryProjection {

    UUID getId();

    String getName();

}
