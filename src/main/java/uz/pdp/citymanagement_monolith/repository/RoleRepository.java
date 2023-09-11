package uz.pdp.citymanagement_monolith.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.entity.RoleEntity;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity,String> {
    List<RoleEntity> findRoleEntitiesByRoleIn(List<String> roles);
}
