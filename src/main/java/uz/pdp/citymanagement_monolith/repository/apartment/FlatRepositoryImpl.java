package uz.pdp.citymanagement_monolith.repository.apartment;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.security.Principal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class FlatRepositoryImpl extends SimpleJpaRepository<FlatEntity, UUID> implements FlatRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public FlatRepositoryImpl(@Autowired Class<FlatEntity> domainClassForFlat, EntityManager em) {
        super(domainClassForFlat, em);
        entityManager = em;
    }

    @Override
    public List<FlatEntity> findAll(Filter filter) {
        try {
            StringBuilder getAll = new StringBuilder("select f from flat f where f.createdTime >= '1800-01-01' ");
            if (filter.getStartDate() != null)
                getAll.append(" and f.createdTime >= '").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime()).append("'");
            if (filter.getEndDate() != null)
                getAll.append(" and f.createdTime <= '").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime()).append("'");
            if(filter.getMaxPrice() != 0)
                getAll.append(" and f.pricePerMonth <= ").append(filter.getMaxPrice()).append("'");
            if(filter.getMinPrice() != 0)
                getAll.append(" and f.pricePerMonth >= ").append(filter.getMinPrice()).append("'");
            if(filter.getType() != null && !filter.getType().isBlank())
                getAll.append(" and f.flatType = '").append(filter.getType()).append("'");
            if(filter.getStatus() != null && !filter.getStatus().isBlank())
                getAll.append(" and f.status = '").append(filter.getStatus()).append("'");
            if(filter.getFloor() != 0)
                getAll.append(" and f.whichFloor = ").append(filter.getFloor()).append("'");
            if(filter.getNumberOfFlats() != 0)
                getAll.append(" and f.rooms = ").append(filter.getNumberOfFlats());
            getAll.append(" group by f.id order by f.pricePerMonth");
            TypedQuery<FlatEntity> query = entityManager.createQuery(getAll.toString(), FlatEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at FlatRepositoryImpl findAll -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<FlatEntity> findByAccommodation(@Nonnull UUID accommodationId,Filter filter) {
        try {
            StringBuilder findByAccommodation = new StringBuilder("select f from flat f where f.accommodation.id = :accId");
            if (filter.getStartDate() != null)
                findByAccommodation.append(" and f.createdTime >= '").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime()).append("'");
            if (filter.getEndDate() != null)
                findByAccommodation.append(" and f.createdTime <= '").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime()).append("'");
            if(filter.getType() != null && !filter.getType().isBlank())
                findByAccommodation.append(" and f.flatType = '").append(filter.getType()).append("'");
            if(filter.getStatus() != null && !filter.getStatus().isBlank())
                findByAccommodation.append(" and f.status = '").append(filter.getStatus()).append("'");
            if(filter.getFloor() != 0)
                findByAccommodation.append(" and f.whichFloor = ").append(filter.getFloor()).append("'");
            if(filter.getNumberOfFlats() != 0)
                findByAccommodation.append(" and f.rooms = ").append(filter.getNumberOfFlats()).append("'");
            if(filter.getMaxPrice() != 0)
                findByAccommodation.append(" and f.pricePerMonth <= ").append(filter.getMaxPrice()).append("'");
            if(filter.getMinPrice() != 0)
                findByAccommodation.append(" and f.pricePerMonth >= ").append(filter.getMinPrice()).append("'");
            TypedQuery<FlatEntity> query = entityManager.createQuery(findByAccommodation.toString(), FlatEntity.class);
            query.setParameter("accId",accommodationId);
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at FlatRepositoryImpl findByAccommodation -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void updateOwner(Principal principal, UUID flatId) {
        String updateOwner = "update flat f set f.owner = (select u from users u where u.email = :email) where f.id = :flatId";
        Query query = entityManager.createQuery(updateOwner);
        query.setParameter("email",principal.getName());
        query.setParameter("flatId",flatId);
        query.executeUpdate();
    }

    @Override
    @Deprecated
    public UUID getFlatCardId(UUID flatId) {
        try {
            String getFlatCardId = "select c.id from flat f join card c on f.card.id = c.id where f.id = :id";
            TypedQuery<UUID> query = entityManager.createQuery(getFlatCardId, UUID.class);
            return query.getSingleResult();
        } catch (Exception e) {
            log.warn("Error at FlatRepositoryImpl -> {}",e.getMessage());
            return null;
        }
    }

    @Override
    public List<FlatEntity> getUsersFlat(Principal principal, Filter filter) {
        try {
            StringBuilder getUsersFlat = new StringBuilder("select f from flat f join users u on f.owner.id = u.id where u.email = :email");
            if (filter.getNumberOfFlats() != 0)
                getUsersFlat.append(" and f.rooms = ").append(filter.getNumberOfFlats());
            if (filter.getFloor() != 0)
                getUsersFlat.append(" and f.whichFloor = ").append(filter.getFloor());
            if (filter.getStartDate() != null)
                getUsersFlat.append(" and f.createdTime >= '").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTS+5"))).append("'");
            if (filter.getEndDate() != null)
                getUsersFlat.append(" and f.createdTime <= '").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTS+5"))).append("'");
            if (filter.getType() != null && !filter.getType().isBlank())
                getUsersFlat.append(" and f.flatType = '").append(filter.getType()).append("'");
            if (filter.getStatus() != null && !filter.getStatus().isBlank())
                getUsersFlat.append(" and f.status = '").append(filter.getStatus()).append("'");
            if(filter.getMaxPrice() != 0)
                getUsersFlat.append(" and f.pricePerMonth <= ").append(filter.getMaxPrice()).append("'");
            if(filter.getMinPrice() != 0)
                getUsersFlat.append(" and f.pricePerMonth >= ").append(filter.getMinPrice()).append("'");
            getUsersFlat.append(" group by f.id order by f.number");
            TypedQuery<FlatEntity> query = entityManager.createQuery(getUsersFlat.toString(), FlatEntity.class);
            query.setParameter("email",principal.getName());
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at FlatRepositoryImpl getUsersFlat -> {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    @Nonnull
    public Optional<FlatEntity> findById(@Nonnull UUID id) {
        try {
            TypedQuery<FlatEntity> query = entityManager.createQuery("select f from flat f where f.id = :id", FlatEntity.class);
            query.setParameter("id", id);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at FlatRepositoryImpl findById -> {}",e.getMessage());
            return Optional.empty();
        }
    }
}
