package uz.pdp.city_management_monolith.domain.dto.apartment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CompanyCreateDto {
    @NotBlank(message = "Name must be present")
    private String name;
    @NotBlank(message = "Description must be present")
    private String description;
    @NotNull(message = "Balance must be present")
    private Double balance;
    @NotNull(message = "Select card")
    private UUID cardId;
}
