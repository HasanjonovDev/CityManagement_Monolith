package uz.pdp.citymanagement_monolith.repository.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.booking.PreOrderBookingEntity;

import java.util.UUID;

@Repository
public class PreOrderBookingRepositoryImpl extends SimpleJpaRepository<PreOrderBookingEntity, UUID> implements PreOrderBookingRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public PreOrderBookingRepositoryImpl(@Autowired Class<PreOrderBookingEntity> domainClassForPreOrders, EntityManager em) {
        super(domainClassForPreOrders, em);
        entityManager = em;
    }
}
