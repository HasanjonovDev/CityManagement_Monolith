package uz.pdp.citymanagement_monolith.domain.entity.payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;

import java.util.UUID;

@Entity(name = "card")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardEntity extends BaseEntity {
    @Column(unique = true,nullable = false)
    private String number;
    private String holderName;
    private Integer pinCode;
    private String expiredDate;
    private Double balance;
    private UUID ownerId;
    @Enumerated(value = EnumType.STRING)
    private CardType type;

}
