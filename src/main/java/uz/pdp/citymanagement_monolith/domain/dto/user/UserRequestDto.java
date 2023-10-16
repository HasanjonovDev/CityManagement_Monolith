package uz.pdp.citymanagement_monolith.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestDto {
    String firstName;
    String lastName;
    @NotBlank(message = "email must not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?^_`{|}~-]+)*@"
            + "[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$")
    String email;

    @NotBlank(message = "password must not be blank")
    String password;
}
