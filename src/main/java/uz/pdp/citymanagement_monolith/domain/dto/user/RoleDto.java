package uz.pdp.citymanagement_monolith.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoleDto {
    @NotBlank(message = "role name cannot be blank")
    private String role;
    private List<PermissionCreateDto> permissions;
}
