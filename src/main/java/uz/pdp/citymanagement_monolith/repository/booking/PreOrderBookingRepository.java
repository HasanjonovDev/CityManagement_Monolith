package uz.pdp.citymanagement_monolith.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.PreOrderBookingEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreOrderBookingRepository extends JpaRepository<PreOrderBookingEntity, UUID> {
    Optional<PreOrderBookingEntity> findPreOrderBookingEntityByFlat(FlatEntity flat);
}
