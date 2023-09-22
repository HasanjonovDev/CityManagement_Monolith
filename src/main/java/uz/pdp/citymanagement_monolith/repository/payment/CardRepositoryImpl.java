package uz.pdp.citymanagement_monolith.repository.payment;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class CardRepositoryImpl extends SimpleJpaRepository<CardEntity, UUID> implements CardRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public CardRepositoryImpl(Class<CardEntity> domainClassForCard, EntityManager em) {
        super(domainClassForCard, em);
        entityManager = em;
    }

    @Override
    public List<CardEntity> findCardEntitiesByOwnerId(UUID id, Filter filter) {
        try {
            StringBuilder findCardEntitiesByOwnerId = new StringBuilder("select c from card c where c.ownerId = '" + id + "' ");
            if (filter.getStartDate() != null)
                findCardEntitiesByOwnerId.append(" and c.createdTime >= '").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime()).append("'");
            if (filter.getEndDate() != null)
                findCardEntitiesByOwnerId.append(" and c.expiredDate <= '").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime()).append("'");
            if (filter.getMaxPrice() != 0)
                findCardEntitiesByOwnerId.append(" and c.balance <= ").append(filter.getMaxPrice());
            if (filter.getMinPrice() != 0)
                findCardEntitiesByOwnerId.append(" and c.balance >= ").append(filter.getMinPrice());
            return entityManager.createQuery(findCardEntitiesByOwnerId.toString(), CardEntity.class).getResultList();
        } catch (Exception e) {
            log.warn("Error at CardRepositoryImpl findCardEntitiesByOwnerId -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<CardEntity> findCardEntityByNumber(String number) {
        try {
            String findCardEntityByNumber = "select c from card c where c.number = :card";
            TypedQuery<CardEntity> query = entityManager.createQuery(findCardEntityByNumber, CardEntity.class);
            query.setParameter("card", number);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at CardRepositoryImpl findCardEntityByNumber -> {}",e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<CardEntity> findCardEntityByOwnerId(UUID ownerId) {
        try {
            String findCardEntityByOwnerId = "select c from card c where c.owner.id = '" + ownerId + "'";
            CardEntity result = entityManager.createQuery(findCardEntityByOwnerId, CardEntity.class).getSingleResult();
            return Optional.of(result);
        } catch (Exception e) {
            log.warn("Error at CardRepositoryImpl findCardEntityByOwnerId -> {}",e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void pay(String senderCardNumber, CardEntity card, Double amount) {
        try {
            String pay = "update card c set c.balance = c.balance - :amount where c.number = :senderNumber";
            Query query = entityManager.createQuery(pay);
            query.setParameter("senderNumber", senderCardNumber);
            query.setParameter("amount", amount);
            query.executeUpdate();
        } catch (Exception e) {
            log.warn("Transaction error! {}", e.getMessage());
            return;
        }
        try{
            String pay = "update card c set c.balance = c.balance + :amount where c.id = :cardId";
            Query query = entityManager.createQuery(pay);
            query.setParameter("cardId",card.getId());
            query.setParameter("amount",amount);
            query.executeUpdate();
        } catch (Exception e) {
            log.warn("Transaction error! {}",e.getMessage());
        }
    }

    @Override
    @Nonnull
    public Optional<CardEntity> findById(@Nonnull UUID uuid) {
        try{
            String findById = "select c from card c where c.id = :cardId";
            TypedQuery<CardEntity> query = entityManager.createQuery(findById, CardEntity.class);
            query.setParameter("cardId",uuid);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at CardRepositoryImpl findById -> {}",e.getMessage());
            return Optional.empty();
        }
    }
}
