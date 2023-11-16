package uz.pdp.city_management_monolith.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.city_management_monolith.domain.entity.post.PostEntity;
import uz.pdp.city_management_monolith.domain.filters.Filter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    Optional<PostEntity> findPostEntityByOwnerId(UUID ownerId);

    List<PostEntity> findPostEntitiesByNameContainingIgnoreCase(String name, Filter filter);
    List<PostEntity> findAll(Filter filter);
}
