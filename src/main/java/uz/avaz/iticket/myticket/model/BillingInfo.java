package uz.avaz.iticket.myticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.avaz.iticket.myticket.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "billing_infos")
public class BillingInfo extends AbsEntity {

    private String cardHolderName;

    private String cardNumber;

    private Integer expirationMonth;

    private Integer expirationYear;

    private String cvcCode;

    @ManyToOne
    private User user;

}
