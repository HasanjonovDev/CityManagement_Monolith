package uz.pdp.citymanagement_monolith.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.citymanagement_monolith.domain.entity.user.MessageState;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInboxForUserDto {
    private UUID id;
    private String message;
    private MessageState state;
}
