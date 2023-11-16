package uz.pdp.city_management_monolith.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RoleForUserDto {
    private String role;
    private List<PermissionsForUserDto> permissions;
}
