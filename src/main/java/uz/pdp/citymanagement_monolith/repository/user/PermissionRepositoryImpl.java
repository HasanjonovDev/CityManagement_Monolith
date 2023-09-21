package uz.pdp.citymanagement_monolith.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class PermissionRepositoryImpl extends SimpleJpaRepository<PermissionEntity, UUID> implements PermissionRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public PermissionRepositoryImpl(@Autowired Class<PermissionEntity> domainClassForPermission, EntityManager em) {
        super(domainClassForPermission, em);
        entityManager = em;
    }

    @Override
    public Optional<PermissionEntity> findByPermission(String permission) {
        try {
            String findByPermission = "select f from permissions f where f.permission = :permission";
            TypedQuery<PermissionEntity> query = entityManager.createQuery(findByPermission, PermissionEntity.class);
            query.setParameter("permission", permission);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at PermissionRepositoryImpl findByPermission -> {}",e.getMessage());
            return Optional.empty();
        }
    }
}
