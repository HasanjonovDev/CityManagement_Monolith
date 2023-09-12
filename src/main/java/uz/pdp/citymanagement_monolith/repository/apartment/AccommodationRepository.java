package uz.pdp.citymanagement_monolith.repository.apartment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.AccommodationEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.CompanyEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccommodationRepository extends JpaRepository<AccommodationEntity, UUID> {
    List<AccommodationEntity> findByCompany(CompanyEntity companyEntity);
}
