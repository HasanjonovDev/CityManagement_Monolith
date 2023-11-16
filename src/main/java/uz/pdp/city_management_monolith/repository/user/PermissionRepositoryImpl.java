package uz.pdp.city_management_monolith.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.city_management_monolith.domain.entity.user.PermissionEntity;

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
}
