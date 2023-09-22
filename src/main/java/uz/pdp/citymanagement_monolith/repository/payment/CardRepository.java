package uz.pdp.citymanagement_monolith.repository.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, UUID> {

    Optional<CardEntity> findCardEntityByNumber(String number);
    Optional<CardEntity> findCardEntityByOwnerId(UUID ownerId);
    List<CardEntity> findCardEntitiesByOwnerId(UUID id, Filter filter);
    void pay(String senderCardNumber, CardEntity card, Double amount);

}
