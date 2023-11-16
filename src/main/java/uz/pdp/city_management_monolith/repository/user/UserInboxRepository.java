package uz.pdp.city_management_monolith.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import uz.pdp.city_management_monolith.domain.entity.user.UserInboxEntity;
import uz.pdp.city_management_monolith.domain.filters.Filter;

import java.util.List;
import java.util.UUID;

@Service
public interface UserInboxRepository extends JpaRepository<UserInboxEntity, UUID> {
    List<UserInboxEntity> getAllByToWhom(UUID owner, Filter filter);
}
