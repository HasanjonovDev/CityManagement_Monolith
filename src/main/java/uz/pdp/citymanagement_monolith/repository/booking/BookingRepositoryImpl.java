package uz.pdp.citymanagement_monolith.repository.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
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
        return entityManager.createQuery("select b from booking b where b.createdTime <= " + createdDate,BookingEntity.class).getResultList();
    }

    @Override
    public List<BookingEntity> findAllByCreatedTimeAfter(LocalDateTime createdTime, Filter filter) {
        StringBuilder findAllByCreatedTimeAfter = new StringBuilder("select b from booking b ");
        if(filter.getStartDate() != null) findAllByCreatedTimeAfter.append(" where b.createdTime >= '").append(filter.getStartDate()).append("'");
        else findAllByCreatedTimeAfter.append(" where b.createdTime >= '").append(createdTime).append("'");
        if(filter.getEndDate() != null) findAllByCreatedTimeAfter.append(" and b.createdTime <= '").append(filter.getEndDate()).append("'");
        if(!filter.getType().isBlank()) findAllByCreatedTimeAfter.append(" and b.type = '").append(filter.getType()).append("'");
        if(!filter.getStatus().isBlank()) findAllByCreatedTimeAfter.append(" and b.status = '").append(filter.getStatus()).append("'");
        if(filter.getFloor() != 0) findAllByCreatedTimeAfter.append(" and ");
        return entityManager.createQuery(findAllByCreatedTimeAfter.toString(),BookingEntity.class).getResultList();
    }

}
