package uz.pdp.citymanagement_monolith.domain.dto.apartment;

import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatStatus;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FlatForUserDto extends BaseEntity {
    private Integer number;
    private Integer whichFloor;
    private FlatType flatType;
    private Integer rooms;
    private UUID ownerId;
    private FlatStatus status;
    private String about;
    private Double pricePerMonth;
}
