package uz.avaz.iticket.myticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.avaz.iticket.myticket.template.AbsEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "price_categories")
public class PriceCategory extends AbsEntity {

    @NotEmpty
    private String name;
    private Double additionalFeeInPercentage;
    private Integer color;

}
