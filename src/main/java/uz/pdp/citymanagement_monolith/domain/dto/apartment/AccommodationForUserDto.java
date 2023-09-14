package uz.pdp.citymanagement_monolith.domain.dto.apartment;

import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.LocationEntity;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AccommodationForUserDto extends BaseEntity {
    private String name;
    private LocationEntity locationEntity;
    private Integer numberOfFlats;
    private Integer floors;
}
