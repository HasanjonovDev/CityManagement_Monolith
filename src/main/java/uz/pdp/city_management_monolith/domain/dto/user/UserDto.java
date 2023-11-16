package uz.pdp.city_management_monolith.domain.dto.user;

import lombok.*;
import uz.pdp.city_management_monolith.domain.entity.user.UserState;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private List<RoleForUserDto> roles;
    private UserState state;
    private int attempts;
}
