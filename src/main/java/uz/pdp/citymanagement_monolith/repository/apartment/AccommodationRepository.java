package uz.pdp.citymanagement_monolith.repository.apartment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.AccommodationEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccommodationRepository extends JpaRepository<AccommodationEntity, UUID> {
    Optional<AccommodationEntity> updateName(String name,UUID accommodationId);
    Optional<AccommodationEntity> updateCompany(UUID accId,UUID comId);
    List<AccommodationEntity> getAll(Filter filter);
}
