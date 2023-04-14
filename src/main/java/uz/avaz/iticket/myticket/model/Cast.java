package uz.avaz.iticket.myticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uz.avaz.iticket.myticket.enums.CastType;
import uz.avaz.iticket.myticket.template.AbsEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "casts")
public class Cast extends AbsEntity {

    @NotEmpty
    private String fullName;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(cascade = CascadeType.ALL)
    private Attachment photo;

    @Enumerated(EnumType.STRING)
    private CastType castType;

}
