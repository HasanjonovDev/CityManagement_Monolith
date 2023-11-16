package uz.pdp.city_management_monolith.repository.apartment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.city_management_monolith.domain.entity.apartment.AccommodationEntity;
import uz.pdp.city_management_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.city_management_monolith.domain.entity.user.UserEntity;
import uz.pdp.city_management_monolith.domain.filters.Filter;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class AccommodationRepositoryImpl extends SimpleJpaRepository<AccommodationEntity, UUID> implements AccommodationRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public AccommodationRepositoryImpl(@Autowired Class<AccommodationEntity> domainClass, EntityManager em) {
        super(domainClass, em);
        entityManager = em;
    }
    public List<AccommodationEntity> findByCompany(CompanyEntity company,Filter filter) {
        try {
            StringBuilder findByCompany = new StringBuilder("select c from accommodations c " +
                    "where c.company.id = " + company.getId());
            TypedQuery<AccommodationEntity> query = generateQuery(filter, findByCompany);
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at AccommodationRepositoryImpl findByCompany -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<AccommodationEntity> getAll(Filter filter) {
        try {
            StringBuilder getAll = new StringBuilder("select f from accommodations f where f.createdTime >= '1800-01-01'");
            if(filter.getStartDate() != null)
                getAll.append(" and f.createdTime >= '").append(filter.getStartDate()).append("'");
            if(filter.getEndDate() != null)
                getAll.append(" and f.createdTime <= '").append(filter.getEndDate()).append("'");

            TypedQuery<AccommodationEntity> query = generateQuery(filter, getAll);
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at AccommodationRepositoryImpl getAll -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<AccommodationEntity> findByCompanyOwner(UserEntity owner) {
        try {
            String findByCompanyOwner = "select a from accommodations a where a.company.owner.id = :ownerId";
            TypedQuery<AccommodationEntity> query = entityManager.createQuery(findByCompanyOwner, AccommodationEntity.class);
            query.setParameter("ownerId",owner.getId());
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at AccommodationRepositoryImpl findByCompanyOwner -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Long getMax() {
        try {
            String getMax = "select max(f.number) from accommodations f";
            TypedQuery<Long> query = entityManager.createQuery(getMax, Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            log.warn("Error at AccommodationRepositoryImpl getMax -> {}",e.getMessage());
            return 0L;
        }
    }

    private TypedQuery<AccommodationEntity> generateQuery(Filter filter, StringBuilder findByCompany) {
        if(filter.getStartDate() != null) findByCompany.append(" and c.createdTime >= ").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
        if(filter.getEndDate() != null) findByCompany.append(" and c.createdTime <= ").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
        return entityManager.createQuery(findByCompany.toString(), AccommodationEntity.class);
    }

    @Override
    @Transactional
    public Optional<AccommodationEntity> updateName(String name,UUID accommodationId) {
        try {
            String updateName = "update accommodations u set u.name = :name where u.id = :id";
            Query query = entityManager.createQuery(updateName);
            query.setParameter("name",name);
            query.setParameter("id",accommodationId);
            query.executeUpdate();
            String getNewOne = "select f from accommodations f where f.id = :accId";
            TypedQuery<AccommodationEntity> query1 = entityManager.createQuery(getNewOne, AccommodationEntity.class);
            query1.setParameter("accId",accommodationId);
            return Optional.of(query1.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at AccommodationRepositoryImpl updateName -> {}",e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<AccommodationEntity> updateCompany(UUID accId, UUID comId) {
        try {
            String updateCompany = "update accommodations a set a.company = (select c from company c where c.id = :comId) where a.id = :accId";
            Query query = entityManager.createQuery(updateCompany);
            query.setParameter("comId", comId);
            query.setParameter("accId", accId);
            query.executeUpdate();
            return Optional.of(entityManager.createQuery("select a from accommodations a where a.id = '" + accId + "'", AccommodationEntity.class).getSingleResult());
        } catch (Exception e) {
            log.warn("Error at AccommodationRepositoryImpl updateCompany -> {}",e.getMessage());
            return Optional.empty();
        }
    }
}
