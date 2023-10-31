package uz.pdp.citymanagement_monolith.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestDto {
    @NotBlank(message = "enter your first name")
    String firstname;
    @NotBlank(message = "enter your first name")
    String lastname;
    @NotBlank(message = "email must not be blank")
    String email;
    @NotBlank(message = "password must not be blank")
    String password;
    @NotNull(message = "enter your birthdate")
    Date birthDate;
}
