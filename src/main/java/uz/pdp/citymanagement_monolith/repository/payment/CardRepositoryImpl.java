package uz.pdp.citymanagement_monolith.repository.payment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CardRepositoryImpl extends SimpleJpaRepository<CardEntity, UUID> implements CardRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public CardRepositoryImpl(Class<CardEntity> domainClassForCard, EntityManager em) {
        super(domainClassForCard, em);
        entityManager = em;
    }


    public List<CardEntity> findCardEntitiesByOwnerId(UUID id, Filter filter) {
        StringBuilder findCardEntitiesByOwnerId = new StringBuilder("select c from card c where c.ownerId = '" + id + "' ");
        if(filter.getStartDate() != null) findCardEntitiesByOwnerId.append(" and c.createdTime >= '").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime()).append("'");
        if(filter.getEndDate() != null) findCardEntitiesByOwnerId.append(" and c.expiredDate <= '").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime()).append("'");
        if(filter.getMaxPrice() != 0) findCardEntitiesByOwnerId.append(" and c.balance <= ").append(filter.getMaxPrice());
        if(filter.getMinPrice() != 0) findCardEntitiesByOwnerId.append(" and c.balance >= ").append(filter.getMinPrice());
        return entityManager.createQuery(findCardEntitiesByOwnerId.toString(), CardEntity.class).getResultList();
    }

    @Override
    public Optional<CardEntity> findCardEntityByNumber(String number) {
        CardEntity result = entityManager.createQuery("select c from card c where c.number = '" + number + "'", CardEntity.class).getSingleResult();
        return Optional.of(result);
    }

    @Override
    public Optional<CardEntity> findCardEntityByOwnerId(UUID ownerId) {
        String findCardEntityByOwnerId = "select c from card c where c.owner.id = '" + ownerId + "'";
        CardEntity result = entityManager.createQuery(findCardEntityByOwnerId, CardEntity.class).getSingleResult();
        return Optional.of(result);
    }
}
