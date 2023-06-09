package uz.avaz.iticket.myticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.avaz.iticket.myticket.model.Country;
import uz.avaz.iticket.myticket.projection.CountryProjection;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "country",collectionResourceRel = "country")
public interface CountryRepository extends JpaRepository<Country, UUID> {

    @Query(nativeQuery = true,value = "SELECT cast(c.id as varchar) as id, c.name as name from countries c join movies_countries mc on c.id = mc.countries_id where mc.movies_id = :id")
    List<CountryProjection> findCountriesByMovieId(UUID id);
}
