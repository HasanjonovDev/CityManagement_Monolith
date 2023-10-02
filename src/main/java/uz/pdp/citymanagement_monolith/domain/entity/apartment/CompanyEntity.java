package uz.pdp.citymanagement_monolith.domain.entity.apartment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

@Entity(name = "company")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CompanyEntity extends BaseEntity {
    @Column(unique = true)
    private String name;
    private String description;
    @ManyToOne(cascade = CascadeType.DETACH)
    private UserEntity owner;
    @ManyToOne(fetch = FetchType.EAGER)
    private CardEntity card;
}
