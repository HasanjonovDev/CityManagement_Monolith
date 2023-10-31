package uz.pdp.citymanagement_monolith.domain.entity.booking;

import jakarta.persistence.Entity;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

import java.util.Date;
import java.util.UUID;

@Entity(name = "pre_order_booking")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PreOrderBookingEntity extends BaseEntity {
    private UUID flatId;
    private Date date;
    private Integer days;
    private Double prePayAmount;
    private UserEntity owner;
}
