package uz.pdp.citymanagement_monolith.domain.dto.apartment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.LocationEntity;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AccommodationCreateDto {
    @NotBlank(message = "Name must be present")
    private String name;
    @NotNull(message = "Company id cannot be null")
    private UUID companyId;
    @NotNull(message = "Location must be present")
    private LocationEntity locationEntity;
}
