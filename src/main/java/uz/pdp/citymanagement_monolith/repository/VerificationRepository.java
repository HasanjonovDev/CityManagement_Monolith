package uz.pdp.citymanagement_monolith.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.VerificationEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationEntity, UUID> {
    Optional<VerificationEntity> findVerificationEntityByUserId(UUID user_id);
}
