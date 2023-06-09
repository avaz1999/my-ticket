package uz.avaz.iticket.myticket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.avaz.iticket.myticket.template.AbsEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "rowss")
public class Row extends AbsEntity {

    private Integer number;
    @ManyToOne
    private Hall hall;
    @JsonIgnore
    @OneToMany(mappedBy = "row",cascade = CascadeType.ALL)
    private List<Seat> seats;

    public Row(Integer number, Hall hall) {
        this.number = number;
        this.hall = hall;
    }

    @Override
    public String toString() {
        return "Row{" +
                "number=" + number +
                '}';
    }
}
