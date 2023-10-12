package uz.pdp.citymanagement_monolith.repository.user;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
public class UserRepositoryImpl extends SimpleJpaRepository<UserEntity, UUID> implements UserRepository {
    @PersistenceContext
    private final EntityManager entityManager;
    public UserRepositoryImpl(Class<UserEntity> domainClass, EntityManager em) {
        super(domainClass, em);
        entityManager = em;
    }

    @Override
    public Optional<UserEntity> findUserEntityByEmail(String email) {
        try{
            String userByEmail = "select u from users u where u.email = :email";
            TypedQuery<UserEntity> query = entityManager.createQuery(userByEmail, UserEntity.class);
            query.setParameter("email", email);
            return Optional.of(query.getSingleResult());
        }catch (Exception e) {
            log.warn("Error at UserRepositoryImpl findUserEntityByEmail -> {}",e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Long countUserEntitiesByEmail(String email) {
        try {
            String count = "select count(u.email) from users u where u.email = :email";
            TypedQuery<Long> query = entityManager.createQuery(count, Long.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            log.warn("Error at UserRepositoryImpl countUserEntitiesByEmail -> {}",e.getMessage());
            return 0L;
        }
    }

    @Override
    @Transactional
    public void changeState(UUID userId, UserState userState) {
        String changeState = "update users u set state = :state where u.id = :id";
        Query query = entityManager.createQuery(changeState);
        query.setParameter("id",userId);
        query.setParameter("state",userState);
        query.executeUpdate();
    }

    @Override
    @Nonnull
    public Optional<UserEntity> findById(@Nonnull UUID userId) {
        try {
            String findById = "select u from users u where u.id = :userId";
            TypedQuery<UserEntity> query = entityManager.createQuery(findById, UserEntity.class);
            query.setParameter("userId", userId);
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            log.warn("Error at UserRepositoryImpl findById -> {}",e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    @Nonnull
    public List<UserEntity> findAll() {
        String findAll = "select u from users u";
        TypedQuery<UserEntity> query = entityManager.createQuery(findAll, UserEntity.class);
        return query.getResultList();
    }
}
