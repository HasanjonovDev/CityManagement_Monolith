package uz.pdp.citymanagement_monolith.domain.entity.apartment;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;


@Entity(name = "flat")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FlatEntity extends BaseEntity {
    private Integer number;
    private Integer whichFloor;
    private FlatType flatType;
    private Integer rooms;
    @ManyToOne
    @JoinColumn(name = "new_owner_id")
    private UserEntity newOwner;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
    @ManyToOne
    @JoinColumn(name = "card_id")
    private CardEntity card;
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private AccommodationEntity accommodation;
    private FlatStatus status;
    private String about;
    private Double pricePerMonth;
    private Double FullPrice;
}
