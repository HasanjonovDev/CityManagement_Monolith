package uz.pdp.citymanagement_monolith.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BuyHistoryEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BuyHistoryRepository extends JpaRepository<BuyHistoryEntity, UUID> {
    Optional<BuyHistoryEntity> findBySeller(UserEntity seller);
    Optional<BuyHistoryEntity> findByBuyer(UserEntity buyer);
}
