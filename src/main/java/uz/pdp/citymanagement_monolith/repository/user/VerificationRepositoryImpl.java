package uz.pdp.citymanagement_monolith.repository.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.user.VerificationEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public class VerificationRepositoryImpl extends SimpleJpaRepository<VerificationEntity, UUID> implements VerificationRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public VerificationRepositoryImpl(Class<VerificationEntity> domainClassForVerification, EntityManager em) {
        super(domainClassForVerification, em);
        entityManager = em;
    }

    @Override
    public Optional<VerificationEntity> findVerificationEntityByUserId(UUID userId) {
        String findVerificationByUserId = "select v from verification v where v.user.id = '" + userId + "'";
        TypedQuery<VerificationEntity> query = entityManager.createQuery(findVerificationByUserId, VerificationEntity.class);
        return Optional.of(query.getSingleResult());
    }
}
