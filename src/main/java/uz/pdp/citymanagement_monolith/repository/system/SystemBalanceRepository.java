package uz.pdp.citymanagement_monolith.repository.system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.system.SystemBalanceEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SystemBalanceRepository extends JpaRepository<SystemBalanceEntity, UUID> {
    Optional<SystemBalanceEntity> findByUser(UserEntity user);
}
