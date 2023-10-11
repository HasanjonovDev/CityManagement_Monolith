package uz.pdp.citymanagement_monolith.repository.apartment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Repository
public interface FlatRepository extends JpaRepository<FlatEntity, UUID> {
    List<FlatEntity> findAll(Filter filter);
    List<FlatEntity> findByAccommodation(UUID accommodationId,Filter filter);
    void updateOwner(Principal principal, UUID flatId);
    UUID getFlatCardId(UUID flatId);
    List<FlatEntity> getUsersFlat(Principal principal,Filter filter);
    List<FlatEntity> findByOwner(UserEntity owner);
}
