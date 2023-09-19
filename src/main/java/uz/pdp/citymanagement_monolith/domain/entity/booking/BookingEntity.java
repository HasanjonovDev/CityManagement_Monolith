package uz.pdp.citymanagement_monolith.domain.entity.booking;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
    @ManyToOne
    @JoinColumn(name = "from_whom_id")
    private UserEntity fromWhom;
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    private BookingType type;
    private Double totalPrice;
    private Long bookingNumber;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
