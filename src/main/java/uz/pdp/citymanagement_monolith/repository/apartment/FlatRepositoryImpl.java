package uz.pdp.citymanagement_monolith.repository.apartment;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FlatRepositoryImpl extends SimpleJpaRepository<FlatEntity, UUID> implements FlatRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public FlatRepositoryImpl(@Autowired Class<FlatEntity> domainClassForFlat, EntityManager em) {
        super(domainClassForFlat, em);
        entityManager = em;
    }

    @Override
    public List<FlatEntity> findAll(Filter filter) {
        String getAll = "select f from flat f where f.createdTime >= '1800-01-01' ";
        if(filter.getStartDate() != null) getAll += " and f.createdTime >= " + filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime();
        if(filter.getEndDate() != null) getAll += " and f.createdTime <= " + filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime();
        TypedQuery<FlatEntity> query = entityManager.createQuery(getAll, FlatEntity.class);
        return query.getResultList();
    }

    @Override
    public List<FlatEntity> findByAccommodation(UUID accommodationId,Filter filter) {
        String findByAccommodation = "select f from flat f where f.accommodation = (select a from accommodation a where a.id = '" + accommodationId + "') ";
        if(filter.getStartDate() != null) findByAccommodation += " and f.createdTime >= " + filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime();
        if(filter.getEndDate() != null) findByAccommodation += " and f.createdTime <= " + filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime();
        return entityManager.createQuery(findByAccommodation, FlatEntity.class).getResultList();
    }
    @Override
    @Nonnull
    public Optional<FlatEntity> findById(@Nonnull UUID id) {
        TypedQuery<FlatEntity> query = entityManager.createQuery("select f from flat f where f.id = :id", FlatEntity.class);
        query.setParameter("id",id);
        return Optional.of(query.getSingleResult());
    }
}
