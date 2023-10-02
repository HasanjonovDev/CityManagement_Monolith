package uz.pdp.citymanagement_monolith.domain.dto.user;

import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.user.RoleEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserState;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserForUserDto {
    private String name;
    private String email;
    private List<RoleEntity> roles;
    private UserState state;
    private int attempts;
}
