package uz.pdp.city_management_monolith.domain.entity.apartment;

import jakarta.persistence.Entity;
import lombok.*;
import uz.pdp.city_management_monolith.domain.entity.BaseEntity;


@Entity(name = "location")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LocationEntity extends BaseEntity {
    private Double longitude;
    private Double latitude;
}
