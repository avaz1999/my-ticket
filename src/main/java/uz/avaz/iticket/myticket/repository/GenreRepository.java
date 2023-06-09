package uz.avaz.iticket.myticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.avaz.iticket.myticket.model.Genre;
import uz.avaz.iticket.myticket.projection.GenreProjection;

import java.util.List;
import java.util.UUID;

@RepositoryRestResource(path = "genre",collectionResourceRel = "genre")
public interface GenreRepository extends JpaRepository<Genre, UUID> {

    @Query(nativeQuery = true,value = "SELECT cast(g.id as varchar) as id , g.name as name from genres g join movies_genres mg on g.id = mg.genres_id where mg.movies_id= :id")
    List<GenreProjection> findGenresByMovieId(UUID id);
}
