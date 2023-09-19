package uz.pdp.citymanagement_monolith.repository.apartment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.AccommodationEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AccommodationRepositoryImpl extends SimpleJpaRepository<AccommodationEntity, UUID> implements AccommodationRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public AccommodationRepositoryImpl(@Autowired Class<AccommodationEntity> domainClass, EntityManager em) {
        super(domainClass, em);
        entityManager = em;
    }
    public List<AccommodationEntity> findByCompany(CompanyEntity company,Filter filter) {
        StringBuilder findByCompany = new StringBuilder("select c from accommodations c " +
                "where c.company.id = " + company.getId());
        TypedQuery<AccommodationEntity> query = generateQuery(filter, findByCompany);
        return query.getResultList();
    }

    @Override
    public List<AccommodationEntity> getAll(Filter filter) {
        StringBuilder getAll = new StringBuilder("select f from accommodations f where f.createdTime >= '1800-01-01'");
        TypedQuery<AccommodationEntity> query = generateQuery(filter, getAll);
        return query.getResultList();
    }

    private TypedQuery<AccommodationEntity> generateQuery(Filter filter, StringBuilder findByCompany) {
        if(filter.getStartDate() != null) findByCompany.append(" and c.createdTime >= ").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
        if(filter.getEndDate() != null) findByCompany.append(" and c.createdTime <= ").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
        return entityManager.createQuery(findByCompany.toString(), AccommodationEntity.class);
    }

    @Override
    @Transactional
    public Optional<AccommodationEntity> updateName(String name,UUID accommodationId) {
        String updateName = "update users u set u.name = '" + name + "'";
        entityManager.createQuery(updateName).executeUpdate();
        return Optional.of(entityManager.createQuery("select a from accommodations a where a.id = '" + accommodationId+"'",AccommodationEntity.class).getSingleResult());
    }

    @Override
    public Optional<AccommodationEntity> updateCompany(UUID accId, UUID comId) {
        String updateCompany = "update accommodations a set a.company = (select c from company c where c.id = :comId) where a.id = :accId";
        Query query = entityManager.createQuery(updateCompany);
        query.setParameter("comId",comId);
        query.setParameter("accId",accId);
        query.executeUpdate();
        return Optional.of(entityManager.createQuery("select a from accommodations a where a.id = '" + accId + "'",AccommodationEntity.class).getSingleResult());
    }
}
