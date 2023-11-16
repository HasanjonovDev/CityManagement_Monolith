package uz.pdp.city_management_monolith.domain.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResetPasswordDto {
    @NotBlank(message = "Enter new password")
    private String newPassword;
    @NotBlank(message = "Confirm your password")
    private String confirmPassword;
}
