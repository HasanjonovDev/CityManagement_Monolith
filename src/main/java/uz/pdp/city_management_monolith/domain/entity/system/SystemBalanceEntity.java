package uz.pdp.city_management_monolith.domain.entity.system;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.city_management_monolith.domain.entity.BaseEntity;
import uz.pdp.city_management_monolith.domain.entity.payment.CardEntity;
import uz.pdp.city_management_monolith.domain.entity.user.UserEntity;

import java.util.UUID;

@Entity(name = "system_balance")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class SystemBalanceEntity extends BaseEntity {
    @ManyToOne(cascade = CascadeType.DETACH)
    private CardEntity senderCard;
    @ManyToOne(cascade = CascadeType.DETACH)
    private CardEntity receiverCard;
    @ManyToOne(cascade = CascadeType.DETACH)
    private UserEntity user;
    private UUID forWhat;
    @Enumerated(EnumType.STRING)
    private SystemBalanceStatus status;
    private Double amount;
}
