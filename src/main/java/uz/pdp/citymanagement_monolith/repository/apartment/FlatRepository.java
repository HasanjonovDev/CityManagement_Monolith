package uz.pdp.citymanagement_monolith.repository.apartment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;


import java.util.UUID;

@Repository
public interface FlatRepository extends JpaRepository<FlatEntity, UUID> {
}
