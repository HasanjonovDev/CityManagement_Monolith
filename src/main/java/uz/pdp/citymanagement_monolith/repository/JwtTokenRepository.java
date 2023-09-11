package uz.pdp.citymanagement_monolith.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.tokens.ScalarToken;
import uz.pdp.citymanagement_monolith.domain.entity.JwtTokenEntity;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtTokenEntity, ScalarToken> {

}
