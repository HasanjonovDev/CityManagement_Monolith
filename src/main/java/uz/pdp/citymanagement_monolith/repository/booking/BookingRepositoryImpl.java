package uz.pdp.citymanagement_monolith.repository.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingType;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class BookingRepositoryImpl extends SimpleJpaRepository<BookingEntity, UUID> implements BookingRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public BookingRepositoryImpl(@Autowired Class<BookingEntity> domainClassForBooking, EntityManager em) {
        super(domainClassForBooking, em);
        entityManager = em;
    }

    @Override
    public int getMax() {
        String getMax = "select max(f.bookingNumber) from booking f";
        return entityManager.createQuery(getMax,Integer.class).getSingleResult();
    }

    @Override
    public List<BookingEntity> findAllByCreatedTimeBefore(LocalDateTime createdDate) {
        try {
            String findAllByCreatedTimeAfter = "select b from booking b where b.createdTime <= :createdTime";
            TypedQuery<BookingEntity> query = entityManager.createQuery(findAllByCreatedTimeAfter, BookingEntity.class);
            query.setParameter("createdTime",createdDate);
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at BookingRepositoryImpl findAllByCreatedTimeBefore -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<BookingEntity> findAllByCreatedTimeAfter(LocalDateTime createdTime, Filter filter) {
        boolean flat = false;
        try {
            StringBuilder findAllByCreatedTimeAfter = new StringBuilder("select b from booking b ");
            if (filter.getFloor() != 0 || filter.getNumberOfFlats() != 0) {
                findAllByCreatedTimeAfter.append(" join flat f on b.orderId = f.id ");
                flat = true;
            }
            if (filter.getStartDate() != null)
                findAllByCreatedTimeAfter.append(" where b.createdTime >= '").append(filter.getStartDate()).append("'");
            else findAllByCreatedTimeAfter.append(" where b.createdTime >= '").append(createdTime).append("'");
            if (filter.getMinPrice() != 0)
                findAllByCreatedTimeAfter.append(" and b.totalPrice >= ").append(filter.getMinPrice());
            if (filter.getMaxPrice() != 0)
                findAllByCreatedTimeAfter.append(" and b.totalPrice <= ").append(filter.getMaxPrice());
            if (filter.getEndDate() != null)
                findAllByCreatedTimeAfter.append(" and b.createdTime <= '").append(filter.getEndDate()).append("'");
            if (filter.getType() != null && !filter.getType().isBlank())
                findAllByCreatedTimeAfter.append(" and b.type = '").append(filter.getType()).append("'");
            if (filter.getStatus() != null && !filter.getStatus().isBlank())
                findAllByCreatedTimeAfter.append(" and b.status = '").append(filter.getStatus()).append("'");
            if (flat && filter.getFloor() != 0)
                findAllByCreatedTimeAfter.append(" and f.whichFloor = ").append(filter.getFloor());
            if (flat && filter.getNumberOfFlats() != 0)
                findAllByCreatedTimeAfter.append(" and f.rooms = ").append(filter.getNumberOfFlats());
            findAllByCreatedTimeAfter.append(" group by b.id, b.type order by b.bookingNumber ASC");
            return entityManager.createQuery(findAllByCreatedTimeAfter.toString(), BookingEntity.class).getResultList();
        } catch (Exception e) {
            log.warn("Error in BookingRepositoryImpl findAllByCreatedTimeAfter method -> {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public int getCount(BookingType type,Filter filter,LocalDateTime createdTime) {
        boolean flat = false;
        try {
            StringBuilder getCount = new StringBuilder();
            if (filter.getFloor() != 0 || filter.getNumberOfFlats() != 0) {
                getCount.append("select count(b) from booking b join flat f on b.orderId = f.id where b.type = ").append(type);
                flat = true;
            } else getCount.append("select count(b) from booking b where b.type = ").append(type);
            if (filter.getStartDate() != null)
                getCount.append(" and b.createdTime >= '").append(filter.getStartDate()).append("'");
            else getCount.append(" and b.createdTime >= '").append(createdTime).append("'");
            if (filter.getMinPrice() != 0) getCount.append(" and b.totalPrice >= ").append(filter.getMinPrice());
            if (filter.getMaxPrice() != 0) getCount.append(" and b.totalPrice <= ").append(filter.getMaxPrice());
            if (filter.getStatus() != null) getCount.append(" and b.status = ").append(filter.getStatus());
            if (filter.getEndDate() != null)
                getCount.append(" and b.createdTime <= '").append(filter.getEndDate()).append("'");
            if (flat && filter.getFloor() != 0)
                getCount.append(" and f.whichFloor = ").append(filter.getFloor());
            if (flat && filter.getNumberOfFlats() != 0)
                getCount.append(" and f.rooms = ").append(filter.getNumberOfFlats());
            getCount.append(" group by b.id, b.type order by b.bookingNumber ASC");
            TypedQuery<Long> query = entityManager.createQuery(getCount.toString(), Long.class);
            return Math.toIntExact(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error in BookingRepositoryImpl getCount method -> {}", e.getMessage());
            return 0;
        }
    }

    @Override
    public Optional<Double> getWeeklyProfit(LocalDateTime weekAgo,Filter filter) {
        boolean flat = false;
        try {
            StringBuilder getWeeklyProfit = new StringBuilder("select sum(b.totalPrice) from booking b ");
            if (filter.getFloor() != 0 || filter.getNumberOfFlats() != 0) {
                getWeeklyProfit.append(" join flat f on b.orderId = f.id ");
                flat = true;
            }
            if (filter.getStartDate() != null)
                getWeeklyProfit.append(" where b.createdTime >= '").append(filter.getStartDate()).append("'");
            else getWeeklyProfit.append(" where b.createdTime >= '").append(weekAgo).append("'");
            if (filter.getMinPrice() != 0) getWeeklyProfit.append(" and b.totalPrice >= ").append(filter.getMinPrice());
            if (filter.getMaxPrice() != 0) getWeeklyProfit.append(" and b.totalPrice <= ").append(filter.getMaxPrice());
            if (filter.getStatus() != null) getWeeklyProfit.append(" and b.status = ").append(filter.getStatus());
            if (filter.getEndDate() != null)
                getWeeklyProfit.append(" and b.createdTime <= '").append(filter.getEndDate()).append("'");
            if (flat && filter.getFloor() != 0)
                getWeeklyProfit.append(" and f.whichFloor = ").append(filter.getFloor());
            if (flat && filter.getNumberOfFlats() != 0)
                getWeeklyProfit.append(" and f.rooms = ").append(filter.getNumberOfFlats());
            getWeeklyProfit.append(" group by b.id, b.type order by b.bookingNumber ASC");
            TypedQuery<Double> query = entityManager.createQuery(getWeeklyProfit.toString(), Double.class);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error in BookingRepositoryImpl getWeeklyProfit method -> {}",e.getMessage());
            return Optional.empty();
        }
    }

}
