package uz.pdp.citymanagement_monolith.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserInboxEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@Slf4j
public class UserInboxRepositoryImpl extends SimpleJpaRepository<UserInboxEntity, UUID> implements UserInboxRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public UserInboxRepositoryImpl(Class<UserInboxEntity> domainClassForUserInboxEntity, EntityManager em) {
        super(domainClassForUserInboxEntity, em);
        entityManager = em;
    }

    @Override
    public List<UserInboxEntity> getAllByOwner(UUID ownerId, Filter filter) {
        try {
            StringBuilder getAllByOwner = new StringBuilder("select f from user_inbox f where f.owner.id = :ownerId");
            if (filter.getStartDate() != null)
                getAllByOwner.append(" and f.createdTime >= '").append(filter.getStartDate()).append("'");
            if (filter.getEndDate() != null)
                getAllByOwner.append(" and f.createdTime <= '").append(filter.getEndDate()).append("'");
            if (filter.getStatus() != null)
                getAllByOwner.append(" and f.status = '").append(filter.getStatus().toUpperCase()).append("'");
            TypedQuery<UserInboxEntity> query = entityManager.createQuery(getAllByOwner.toString(), UserInboxEntity.class);
            query.setParameter("ownerId", ownerId);
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at UserInboxRepositoryImpl getAllByOwner -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }
}
