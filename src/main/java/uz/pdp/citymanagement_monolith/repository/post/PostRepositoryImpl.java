package uz.pdp.citymanagement_monolith.repository.post;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.post.PostEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class PostRepositoryImpl extends SimpleJpaRepository<PostEntity, UUID> implements PostRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public PostRepositoryImpl(@Autowired Class<PostEntity> domainClass, EntityManager em) {
        super(domainClass, em);
        entityManager = em;
    }

    @Override
    public Optional<PostEntity> findPostEntityByOwnerId(UUID ownerId) {
        try {
            String findPostEntityByOwnerId = "select r from posts r where r.owner.id = :ownerId";
            TypedQuery<PostEntity> query = entityManager.createQuery(findPostEntityByOwnerId, PostEntity.class);
            query.setParameter("ownerId", ownerId);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at PostRepositoryImpl findPostEntityByOwnerId -> {}",e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<PostEntity> findPostEntitiesByNameContainingIgnoreCase(String name, Filter filter) {
        try {
            StringBuilder findPostEntitiesByNameContainingIgnoreCase = new StringBuilder("select p from posts p where p.name ilike '").append(name).append("'")
                    .append(" or p.description ilike '%").append(name).append("%'");
            if(filter.getStartDate() != null)
                findPostEntitiesByNameContainingIgnoreCase.append(" and p.createdTime >= '").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
            if(filter.getEndDate() != null)
                findPostEntitiesByNameContainingIgnoreCase.append(" and p.createdTime <= '").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
            if(filter.getStatus() != null)
                findPostEntitiesByNameContainingIgnoreCase.append(" and p.status = '").append(filter.getStatus()).append("'");
            if(filter.getMinPrice() != 0)
                findPostEntitiesByNameContainingIgnoreCase.append(" and p.price >= ").append(filter.getMinPrice());
            if(filter.getMaxPrice() != 0)
                findPostEntitiesByNameContainingIgnoreCase.append(" and p.price <= ").append(filter.getMaxPrice());
            findPostEntitiesByNameContainingIgnoreCase.append(" order by p.name");
            TypedQuery<PostEntity> query = entityManager.createQuery(findPostEntitiesByNameContainingIgnoreCase.toString(), PostEntity.class);
            return query.getResultList();
        } catch (Exception e) {
            log.warn("Error at PostRepositoryImpl findPostEntitiesByNameContainingIgnoreCase -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    @Nonnull
    public List<PostEntity> findAll(@Nonnull Filter filter) {
        try {
            StringBuilder findAll = new StringBuilder("select p from posts p where p.createdTime >= '1800.01.01'");
            if(filter.getStartDate() != null)
                findAll.append(" and p.createdTime >= '").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
            if(filter.getEndDate() != null)
                findAll.append(" and p.createdTime <= '").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
            if(filter.getStatus() != null)
                findAll.append(" and p.status = '").append(filter.getStatus()).append("'");
            if(filter.getMinPrice() != 0)
                findAll.append(" and p.price >= ").append(filter.getMinPrice());
            if(filter.getMaxPrice() != 0)
                findAll.append(" and p.price <= ").append(filter.getMaxPrice());
            findAll.append(" order by p.name");
            TypedQuery<PostEntity> query = entityManager.createQuery(findAll.toString(), PostEntity.class);
            return query.getResultList();
        }catch (Exception e) {
            log.warn("Error at PostRepositoryImpl findAll -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }
}
