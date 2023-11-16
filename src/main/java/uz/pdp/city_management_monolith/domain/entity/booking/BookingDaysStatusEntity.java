package uz.pdp.city_management_monolith.domain.entity.booking;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.city_management_monolith.domain.entity.BaseEntity;
import uz.pdp.city_management_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.city_management_monolith.domain.entity.apartment.FlatStatus;

import java.util.Date;

@Entity(name = "booking_days_status")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class BookingDaysStatusEntity extends BaseEntity {
    @ManyToOne(cascade = CascadeType.DETACH)
    private FlatEntity flat;
    private Date date;
    @Enumerated(EnumType.STRING)
    private FlatStatus status;
}
