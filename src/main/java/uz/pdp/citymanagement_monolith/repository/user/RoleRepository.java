package uz.pdp.citymanagement_monolith.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.RoleEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    List<RoleEntity> findRoleEntitiesByRoleIn(List<String> roles);
    Optional<RoleEntity> findRoleEntityByRole(String role);
    @Query("select r.permissions from role r where r.id = :id")
    List<PermissionEntity> permissions(@Param("id") UUID roleId);
    Optional<RoleEntity> findRoleEntitiesByPermissionsIn(Collection<List<PermissionEntity>> permissions);
}
