package uz.pdp.citymanagement_monolith.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findUserEntityByEmail(String email);
    Long countUserEntitiesByEmail(String email);

    void changeState(UUID userId, UserState userState);
//    List<UserEntity> findUserEntitiesByRoles(List<RoleEntity> roles);
}
