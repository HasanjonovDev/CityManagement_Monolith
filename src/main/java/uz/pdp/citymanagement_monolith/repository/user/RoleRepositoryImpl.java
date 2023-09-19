package uz.pdp.citymanagement_monolith.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.RoleEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepositoryImpl extends SimpleJpaRepository<RoleEntity, UUID> implements RoleRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public RoleRepositoryImpl(@Autowired Class<RoleEntity> domainClassForRole, EntityManager em) {
        super(domainClassForRole, em);
        entityManager = em;
    }

    @Override
    public Optional<RoleEntity> findRoleEntityByRole(String role) {
        String findRoleEntityByRole = "select r from role r where r.role = '" + role + "'";
        RoleEntity singleResult = entityManager.createQuery(findRoleEntityByRole, RoleEntity.class).getSingleResult();
        return Optional.of(singleResult);
    }

    public List<PermissionEntity> permissions(UUID roleId, Filter filter) {
        StringBuilder permissions = new StringBuilder("select r.permissions from role r where r.id = '" + roleId + "'");
        if(filter.getStartDate() != null) permissions.append(" and r.createdDate >= ").append(filter.getStartDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
        if(filter.getEndDate() != null) permissions.append(" and r.createdDate >= ").append(filter.getEndDate().toInstant().atZone(ZoneId.of("UTC+5")).toLocalDateTime());
        return entityManager.createQuery(permissions.toString(),PermissionEntity.class).getResultList();
    }
}
