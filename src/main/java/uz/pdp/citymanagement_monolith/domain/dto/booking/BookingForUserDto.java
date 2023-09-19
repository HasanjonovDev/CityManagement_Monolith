package uz.pdp.citymanagement_monolith.domain.dto.booking;

import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingStatus;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingType;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BookingForUserDto {
    private UUID ownerId;
    private UUID fromWhomId;
    private UUID orderId;
    private BookingType type;
    private Double totalPrice;
    private Long bookingNumber;
    private LocalDateTime endTime;
    private BookingStatus status;
}
