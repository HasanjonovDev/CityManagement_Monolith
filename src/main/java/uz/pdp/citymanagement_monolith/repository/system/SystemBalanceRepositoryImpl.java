package uz.pdp.citymanagement_monolith.repository.system;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.system.SystemBalanceEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class SystemBalanceRepositoryImpl extends SimpleJpaRepository<SystemBalanceEntity, UUID> implements SystemBalanceRepository {
    @PersistenceContext
    private EntityManager entityManager;
    public SystemBalanceRepositoryImpl(@Autowired Class<SystemBalanceEntity> domainClassForSystemBalance, EntityManager em) {
        super(domainClassForSystemBalance, em);
        entityManager = em;
    }

    @Override
    public Optional<SystemBalanceEntity> findByUser(UserEntity user) {
        try {
            String findByUser = "select f from system_balance f where f.user.id = :userId";
            TypedQuery<SystemBalanceEntity> query = entityManager.createQuery(findByUser, SystemBalanceEntity.class);
            query.setParameter("userId",user.getId());
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at SystemBalanceRepositoryImpl findByUser -> {}",e.getMessage());
            return Optional.empty();
        }
    }
}
