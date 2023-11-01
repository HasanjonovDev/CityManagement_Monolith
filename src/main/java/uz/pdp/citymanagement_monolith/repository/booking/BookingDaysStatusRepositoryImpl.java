package uz.pdp.citymanagement_monolith.repository.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingDaysStatusEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class BookingDaysStatusRepositoryImpl extends SimpleJpaRepository<BookingDaysStatusEntity, UUID> implements BookingDaysStatusRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public BookingDaysStatusRepositoryImpl(@Autowired Class<BookingDaysStatusEntity> domainClassForBookingDayStatus, EntityManager em) {
        super(domainClassForBookingDayStatus, em);
        entityManager = em;
    }

    @Override
    public Optional<BookingDaysStatusEntity> findBookingDaysStatusEntityByFlat(FlatEntity flat) {
        try {
            String findBookingDaysStatusEntityByFlat = "select f from booking_days_status f where f.flat.id = :flatId";
            TypedQuery<BookingDaysStatusEntity> query = entityManager.createQuery(findBookingDaysStatusEntityByFlat, BookingDaysStatusEntity.class);
            query.setParameter("flatId",flat.getId());
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at BookingDaysStatusRepositoryImpl findBookingDaysStatusEntityByFlat -> {}",e.getMessage());
            return Optional.empty();
        }
    }
}
