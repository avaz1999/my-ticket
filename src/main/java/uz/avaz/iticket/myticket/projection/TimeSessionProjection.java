package uz.avaz.iticket.myticket.projection;

import java.time.LocalTime;
import java.util.UUID;

public interface TimeSessionProjection {

    UUID getSessionId();

    LocalTime getStartTime();
}
