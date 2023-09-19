package uz.pdp.citymanagement_monolith.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findUserEntityByEmail(String email);
    Long countUserEntitiesByEmail(String email);
//    List<UserEntity> findUserEntitiesByRoles(List<RoleEntity> roles);
}
