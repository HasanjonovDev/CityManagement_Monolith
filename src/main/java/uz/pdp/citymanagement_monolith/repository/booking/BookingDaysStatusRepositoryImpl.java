package uz.pdp.citymanagement_monolith.repository.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingDaysStatusEntity;

import java.util.UUID;

@Repository
public class BookingDaysStatusRepositoryImpl extends SimpleJpaRepository<BookingDaysStatusEntity, UUID> implements BookingDaysStatusRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public BookingDaysStatusRepositoryImpl(@Autowired Class<BookingDaysStatusEntity> domainClassForBookingDayStatus, EntityManager em) {
        super(domainClassForBookingDayStatus, em);
        entityManager = em;
    }
}
