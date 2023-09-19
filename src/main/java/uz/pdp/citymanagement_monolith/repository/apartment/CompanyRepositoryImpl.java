package uz.pdp.citymanagement_monolith.repository.apartment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;



@Repository
public class CompanyRepositoryImpl extends SimpleJpaRepository<CompanyEntity, UUID> implements CompanyRepository {
    @PersistenceContext
    private final EntityManager entityManager;


    public CompanyRepositoryImpl(@Autowired Class<CompanyEntity> domainClassForCompany, EntityManager em) {
        super(domainClassForCompany, em);
        entityManager = em;
    }

    @Override
    public Optional<CompanyEntity> findByOwnerId(UUID ownerId) {
        String findByOwnerId = "select c from company c where c.owner.id = :ownerId";
        TypedQuery<CompanyEntity> query = entityManager.createQuery(findByOwnerId, CompanyEntity.class);
        query.setParameter("ownerId",ownerId);
        return Optional.of(query.getSingleResult());
    }

    @Override
    public List<CompanyEntity> findCompanyEntitiesByOwnerId(UUID ownerId, Filter filter) {
        String findCompaniesByOwnerId = "select c from company c where c.owner.id = '" + ownerId + "'";
        if(filter.getStartDate() != null) findCompaniesByOwnerId += " and c.createdTime >= '" + filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime() + "'";
        if(filter.getEndDate() != null) findCompaniesByOwnerId += " and c.createdTime <= '" + filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime() + "'";
        TypedQuery<CompanyEntity> query = entityManager.createQuery(findCompaniesByOwnerId, CompanyEntity.class);
        query.setParameter("ownerId",ownerId);
        return query.getResultList();
    }

}
