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
    private Date expiredDate;
    private Double balance;
    @ManyToOne(cascade = CascadeType.DETACH)
//    @JoinColumn(name = "owner_id",table = "users",referencedColumnName = "id",)
    private UserEntity owner;
    @Enumerated(value = EnumType.STRING)
    private CardType type;
}
