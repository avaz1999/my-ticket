package uz.avaz.iticket.myticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.avaz.iticket.myticket.model.Cast;
import uz.avaz.iticket.myticket.projection.CastProjection;

import java.util.List;
import java.util.UUID;
//@RepositoryRestResource(path = "/api/cast",collectionResourceRel = "cast")
public interface CastRepository extends JpaRepository<Cast, UUID> {

    @Query(nativeQuery = true,value = "SELECT cast(c.id as varchar) as id, c.full_name as fullName, c.cast_type as castType from casts c join movies_casts mc on c.id = mc.casts_id where mc.movies_id= :id")
    List<CastProjection> findCastsByMovieId(UUID id);
}
