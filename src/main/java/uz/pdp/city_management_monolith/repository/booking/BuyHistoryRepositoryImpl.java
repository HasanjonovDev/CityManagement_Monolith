package uz.pdp.city_management_monolith.repository.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.city_management_monolith.domain.entity.booking.BuyHistoryEntity;
import uz.pdp.city_management_monolith.domain.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class BuyHistoryRepositoryImpl extends SimpleJpaRepository<BuyHistoryEntity, UUID> implements BuyHistoryRepository {
    @PersistenceContext
    private EntityManager entityManager;
    public BuyHistoryRepositoryImpl(@Autowired Class<BuyHistoryEntity> domainClassForBuyHistory, EntityManager em) {
        super(domainClassForBuyHistory, em);
        entityManager = em;
    }

    @Override
    public Optional<BuyHistoryEntity> findByBuyer(UserEntity buyer) {
        try {
            String findByBuyer = "select f from buy_history f where f.buyer.id = :buyerId";
            TypedQuery<BuyHistoryEntity> query = entityManager.createQuery(findByBuyer, BuyHistoryEntity.class);
            query.setParameter("buyerId",buyer.getId());
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at BuyHistoryRepositoryImpl findByBuyer -> {}",e.getMessage());
            return Optional.empty();
        }
    }
}
