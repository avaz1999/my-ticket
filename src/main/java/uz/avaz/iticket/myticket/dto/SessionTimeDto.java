package uz.avaz.iticket.myticket.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionTimeDto {
    private String startTime;
    private double sessionAdditionalFeeInPercent;
}
