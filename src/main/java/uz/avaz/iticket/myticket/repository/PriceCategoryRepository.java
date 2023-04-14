package uz.avaz.iticket.myticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.avaz.iticket.myticket.model.PriceCategory;

import java.util.UUID;

@RepositoryRestResource(path = "price-category",collectionResourceRel = "price-categories", itemResourceRel = "price-category")
public interface PriceCategoryRepository extends JpaRepository<PriceCategory, UUID> {
    PriceCategory findPriceCategoriesById(UUID id);
}
