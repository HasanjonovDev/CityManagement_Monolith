package uz.pdp.city_management_monolith.domain.entity.booking;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.pdp.city_management_monolith.domain.entity.BaseEntity;
import uz.pdp.city_management_monolith.domain.entity.user.UserEntity;

import java.util.UUID;

@Entity(name = "buy_history")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BuyHistoryEntity extends BaseEntity {
    @ManyToOne(cascade = CascadeType.DETACH)
    private UserEntity buyer;
    @ManyToOne(cascade = CascadeType.DETACH)
    private UserEntity seller;
    private UUID good;
    private Double amount;
    private BuyHistoryStatus status;
}
