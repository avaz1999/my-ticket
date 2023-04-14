package uz.avaz.iticket.myticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.avaz.iticket.myticket.template.AbsEntity;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class RefundChargeFee extends AbsEntity {


    private Integer intervalInMinutes;


    private Double chargeFeeInPercentage;

    
}
