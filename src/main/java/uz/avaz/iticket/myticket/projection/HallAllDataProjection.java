package uz.avaz.iticket.myticket.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uz.avaz.iticket.myticket.model.Hall;

import java.util.List;
import java.util.UUID;

@Projection(types = {Hall.class},name = "hallAllDataProjection")
public interface HallAllDataProjection {

    UUID getId();

    String getName();

    Double getVipAdditionalFeeInPercent();

    @Value( "#{@rowRepository.findRowsByHallId({target.id})}")
    List<RowAllDataProjection> getRows();

}
