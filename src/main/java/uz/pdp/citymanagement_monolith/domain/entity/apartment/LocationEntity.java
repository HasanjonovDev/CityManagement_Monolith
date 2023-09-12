package uz.pdp.citymanagement_monolith.domain.entity.apartment;

import jakarta.persistence.Entity;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;


@Entity(name = "locations")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LocationEntity extends BaseEntity {
    private Double longitude;
    private Double latitude;
}
