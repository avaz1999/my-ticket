package uz.avaz.iticket.myticket.projection;

import org.springframework.data.rest.core.config.Projection;
import uz.avaz.iticket.myticket.model.Distributor;

import java.util.UUID;

@Projection(types = {Distributor.class})
public interface DistributorProjection {

    UUID getId();

    String getName();

}
