package uz.pdp.citymanagement_monolith.domain.dto.user;

import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserState;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserDto {
    private String name;
    private String email;
    private List<RoleForUserDto> roles;
    private UserState state;
}
