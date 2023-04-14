package uz.avaz.iticket.myticket.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import uz.avaz.iticket.myticket.model.Movie;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Projection(types = {Movie.class},name = "movie")
public interface MovieProjection {

    UUID getId();

    UUID getPosterImageId();

    String getTitle();

    String getDescription();

    Integer getDurationInMin();

    String getTrailerVideoUrl();

    LocalDate getReleaseDate();

    Double getBudget();

    @Value( "#{@castRepository.findCastsByMovieId({target.id})}")
    List<CastProjection> getCasts();

    @Value( "#{@countryRepository.findCountriesByMovieId({target.id})}")
    List<CountryProjection> getCountries();

    @Value( "#{@genreRepository.findGenresByMovieId({target.id})}")
    List<GenreProjection> getGenres();

    @Value( "#{@distributorRepository.findDistributorsByMovieId({target.id})}")
    List<DistributorProjection> getDistributors();
}
