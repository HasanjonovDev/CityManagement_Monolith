package uz.pdp.city_management_monolith.repository.apartment;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.city_management_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.city_management_monolith.domain.filters.Filter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Repository
@Slf4j
public class CompanyRepositoryImpl extends SimpleJpaRepository<CompanyEntity, UUID> implements CompanyRepository {
    @PersistenceContext
    private final EntityManager entityManager;


    public CompanyRepositoryImpl(@Autowired Class<CompanyEntity> domainClassForCompany, EntityManager em) {
        super(domainClassForCompany, em);
        entityManager = em;
    }

    @Override
    public List<CompanyEntity> findCompanyEntitiesByOwnerId(UUID ownerId, Filter filter) {
        try {
            StringBuilder findCompaniesByOwnerId = new StringBuilder("select c from company c where c.owner.id = :id");
            if (filter.getStartDate() != null)
                findCompaniesByOwnerId.append(" and c.createdTime >= '").append(filter.getStartDate()).append("'");
            if (filter.getEndDate() != null)
                findCompaniesByOwnerId.append(" and c.createdTime <= '").append(filter.getEndDate()).append("'");
            TypedQuery<CompanyEntity> query = entityManager.createQuery(findCompaniesByOwnerId.toString(), CompanyEntity.class);
            query.setParameter("id",ownerId);
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at CompanyRepositoryImpl findCompanyEntitiesByOwnerId -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<CompanyEntity> findAll(Filter filter) {
        try {
            StringBuilder findAll = new StringBuilder("select c from company c where c.createdTime >= '1800-01-01' ");
            if(filter.getStartDate() != null) findAll.append(" and c.createdTime >= '").append(filter.getStartDate()).append("'");
            if(filter.getEndDate() != null) findAll.append(" and c.createdTime <= '").append(filter.getEndDate()).append("'");
            if(filter.getEndDate() != null) findAll.append(" and c.createdTime <= '").append(filter.getEndDate()).append("'");
            TypedQuery<CompanyEntity> query = entityManager.createQuery(findAll.toString(), CompanyEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at CompanyRepositoryImpl findAll -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }

}
