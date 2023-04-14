package uz.avaz.iticket.myticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HallDto {
    private String name;
    private Double vipAdditionalFeeInPercent;
    private List<RowDto> rows;
}
