package uz.avaz.iticket.myticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RowDto {
    private Integer number;
    private List<SeatDto> seats;
}
