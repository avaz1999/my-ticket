package uz.avaz.iticket.myticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.avaz.iticket.myticket.enums.PurchaseStatus;
import uz.avaz.iticket.myticket.template.AbsEntity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "purchase_histories")
public class PurchaseHistory extends AbsEntity {

    @ManyToMany
    @JoinTable(
            name = "purchase_histories_tickets",
            joinColumns = @JoinColumn(name = "purchase_history_id"),
            inverseJoinColumns = @JoinColumn(name = "ticket_id"))
    private List<Ticket> tickets;

    private Double totalAmount;

    @ManyToOne
    private PayType payType;

    String paymentIntentId;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus purchaseStatus;


}
