package uz.pdp.citymanagement_monolith.domain.entity.booking;

import jakarta.persistence.Entity;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatStatus;

import java.util.Date;
import java.util.UUID;

@Entity(name = "booking_days_status")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BookingDaysStatusEntity extends BaseEntity {
    private UUID flatId;
    private Date date;
    private FlatStatus status;
}
