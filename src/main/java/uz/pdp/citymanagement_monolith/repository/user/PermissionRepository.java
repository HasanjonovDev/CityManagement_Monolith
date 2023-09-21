package uz.pdp.citymanagement_monolith.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, UUID> {
    Optional<PermissionEntity> findByPermission(String permission);
}
