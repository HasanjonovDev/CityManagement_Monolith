package uz.pdp.citymanagement_monolith.domain.entity.booking;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "booking")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingEntity extends BaseEntity {
    private UUID ownerId;
    private UUID fromWhomId;
    private UUID orderId;
    @Enumerated(EnumType.STRING)
    private BookingType type;
    private Double totalPrice;
    private Long bookingNumber;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
