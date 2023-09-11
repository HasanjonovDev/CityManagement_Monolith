package uz.pdp.citymanagement_monolith.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findUserEntityByEmail(String email);
    Integer countUserEntitiesByEmail(String email);
    Optional<UserEntity>findUserEntityById(UUID id);
    List<UserEntity> findUserEntitiesByRoles(String role);
}
