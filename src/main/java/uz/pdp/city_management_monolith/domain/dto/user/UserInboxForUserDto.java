package uz.pdp.city_management_monolith.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInboxForUserDto {
    private UUID id;
    private String message;
    private String state;
    private String type;
    private UserForUserDto fromWhom;
    private UserForUserDto toWhom;
}
