package uz.pdp.city_management_monolith.repository.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.city_management_monolith.domain.entity.booking.BookingEntity;
import uz.pdp.city_management_monolith.domain.entity.booking.BookingType;
import uz.pdp.city_management_monolith.domain.filters.Filter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {

    int getMax();
    List<BookingEntity> findAllByCreatedTimeBefore(LocalDateTime createdDate);
    List<BookingEntity> findAllByCreatedTimeAfter(LocalDateTime createdTime, Filter filter);
    int getCount(BookingType type,Filter filter,LocalDateTime createdTime);
    Optional<Double> getWeeklyProfit(LocalDateTime weekAgo,Filter filter);
}
