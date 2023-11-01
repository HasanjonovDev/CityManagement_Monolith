package uz.pdp.citymanagement_monolith.repository.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.PreOrderBookingEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class PreOrderBookingRepositoryImpl extends SimpleJpaRepository<PreOrderBookingEntity, UUID> implements PreOrderBookingRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public PreOrderBookingRepositoryImpl(@Autowired Class<PreOrderBookingEntity> domainClassForPreOrders, EntityManager em) {
        super(domainClassForPreOrders, em);
        entityManager = em;
    }

    @Override
    public Optional<PreOrderBookingEntity> findPreOrderBookingEntityByFlat(FlatEntity flat) {
        try {
            String findPreOrderBookingEntityByFlat = "select f from pre_order_booking f where f.flat.id = :flatId";
            TypedQuery<PreOrderBookingEntity> query = entityManager.createQuery(findPreOrderBookingEntityByFlat, PreOrderBookingEntity.class);
            query.setParameter("flatId",flat.getId());
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at PreOrderBookingRepositoryImpl findPreOrderBookingEntityByFlat -> {}",e.getMessage());
            return Optional.empty();
        }
    }
}
