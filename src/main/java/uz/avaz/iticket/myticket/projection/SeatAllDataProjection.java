package uz.avaz.iticket.myticket.projection;

import java.util.UUID;

public interface SeatAllDataProjection {
    UUID getId();

    Integer getNumber();

    UUID getPriceCategoryId();
}
