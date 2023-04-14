package uz.avaz.iticket.myticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.avaz.iticket.myticket.enums.MovieStatus;
import uz.avaz.iticket.myticket.template.AbsEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "movies")
public class Movie extends AbsEntity {

    @NotEmpty
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Integer durationInMin;

    private double  ticketInitPrice;

    @Column(nullable = false)
    private String trailerVideoUrl;

    @Column(columnDefinition = "date")
    private LocalDate releaseDate;

    private Double budget;

    @Enumerated(value = EnumType.STRING)
    private MovieStatus movieStatus;

    @Column(nullable = false)
    private Double distributorShareInPercentage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Attachment posterImg;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Cast> casts;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Genre> genres;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Country> countries;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Distributor> distributors;




}
