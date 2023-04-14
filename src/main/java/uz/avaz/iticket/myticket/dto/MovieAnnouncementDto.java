package uz.avaz.iticket.myticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieAnnouncementDto {
    private UUID movieId;
    private boolean isActive;
}
