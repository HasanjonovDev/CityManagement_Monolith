package uz.pdp.citymanagement_monolith.domain.dto.apartment;

import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.LocationEntity;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AccommodationForUserDto extends BaseEntity {
    private UUID id;
    private String name;
    private Long number;
    private LocationEntity locationEntity;
    private CompanyForUserDto company;
    private Integer numberOfFlats;
    private Integer floors;
    private String imgPath;
}
