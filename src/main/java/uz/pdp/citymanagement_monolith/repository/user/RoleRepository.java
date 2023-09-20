package uz.pdp.citymanagement_monolith.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.RoleEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findRoleEntityByRole(String role);


    List<PermissionEntity> permissions(UUID roleId, Filter filter);
}
