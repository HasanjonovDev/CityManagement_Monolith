package uz.pdp.citymanagement_monolith.repository.apartment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {
    Optional<CompanyEntity> findByOwnerId (UUID ownerId);
    List<CompanyEntity> findCompanyEntitiesByOwnerId(UUID ownerId, Filter filter);
    List<CompanyEntity> findAll(Filter filter);
}
