package uz.avaz.iticket.myticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.avaz.iticket.myticket.model.Distributor;
import uz.avaz.iticket.myticket.projection.DistributorProjection;

import java.util.List;
import java.util.UUID;

public interface DistributorRepository extends JpaRepository<Distributor, UUID> {
    Distributor findDistributorById(UUID id);

    @Query(nativeQuery = true,value = "SELECT cast(d.id as varchar) as id, d.name as name from distributors d join movies_distributors md on d.id = md.distributors_id where md.movies_id = :id")
    List<DistributorProjection> findDistributorsByMovieId(UUID id);
}
