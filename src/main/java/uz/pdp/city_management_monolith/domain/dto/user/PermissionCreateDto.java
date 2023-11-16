package uz.pdp.city_management_monolith.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PermissionCreateDto {
    @NotBlank(message = "Permission cannot be blank!")
    private String permission;
}
