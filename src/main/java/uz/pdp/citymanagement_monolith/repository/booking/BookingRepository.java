package uz.pdp.citymanagement_monolith.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {
    @Query("select max(r.bookingNumber) from booking r")
    int getMax();
    List<BookingEntity> findAllByCreatedTimeBefore(LocalDateTime createdDate);
    List<BookingEntity> findAllByCreatedTimeAfter(LocalDateTime createdTime);
}
