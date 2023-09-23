package uz.pdp.citymanagement_monolith.domain.entity.payment;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

import java.util.Date;

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
    private Integer month;
    private Integer year;
    private Double balance;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
    @Enumerated(value = EnumType.STRING)
    private CardType type;
}
