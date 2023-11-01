package uz.pdp.citymanagement_monolith.domain.entity.booking;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

import java.util.Date;

@Entity(name = "pre_order_booking")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PreOrderBookingEntity extends BaseEntity {
    @ManyToOne(cascade = CascadeType.DETACH)
    private FlatEntity flat;
    private Date date;
    private Integer days;
    private Double prePayAmount;
    @ManyToOne(cascade = CascadeType.DETACH)
    private UserEntity owner;
}
