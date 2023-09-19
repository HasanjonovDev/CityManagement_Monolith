package uz.pdp.citymanagement_monolith.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;

import java.util.UUID;

@Repository
public class PermissionRepositoryImpl extends SimpleJpaRepository<PermissionEntity, UUID> implements PermissionRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public PermissionRepositoryImpl(@Autowired Class<PermissionEntity> domainClassForPermission, EntityManager em) {
        super(domainClassForPermission, em);
        entityManager = em;
    }
}
