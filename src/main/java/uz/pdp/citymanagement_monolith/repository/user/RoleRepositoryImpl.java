package uz.pdp.citymanagement_monolith.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.RoleEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class RoleRepositoryImpl extends SimpleJpaRepository<RoleEntity, UUID> implements RoleRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public RoleRepositoryImpl(@Autowired Class<RoleEntity> domainClassForRole, EntityManager em) {
        super(domainClassForRole, em);
        entityManager = em;
    }

    @Override
    public Optional<RoleEntity> findRoleEntityByRole(String role) {
        try {
            String findRoleEntityByRole = "select r from roles r where r.role = :role";
            TypedQuery<RoleEntity> query = entityManager.createQuery(findRoleEntityByRole, RoleEntity.class);
            query.setParameter("role", role);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at RoleRepositoryImpl findRoleEntityByRole -> {}",e.getMessage());
            return Optional.empty();
        }
    }
    @Override
    public List<PermissionEntity> permissions(UUID roleId, Filter filter) {
        try {
            StringBuilder permissions = new StringBuilder("select r.permissions from role r where r.id = '" + roleId + "'");
            if (filter.getStartDate() != null)
                permissions.append(" and r.createdDate >= ").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
            if (filter.getEndDate() != null)
                permissions.append(" and r.createdDate >= ").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
            return entityManager.createQuery(permissions.toString(), PermissionEntity.class).getResultList();
        } catch (Exception e) {
            log.warn("Error at RoleRepositoryImpl permissions -> {}",e.getMessage());
            return new ArrayList<>();
        }
    }
}
