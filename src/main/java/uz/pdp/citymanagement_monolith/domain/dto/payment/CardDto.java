package uz.pdp.citymanagement_monolith.domain.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardDto {
   @NotBlank(message = "number is blank please try again")
    private String number;
    @NotBlank(message = "Holder name is blank please try again")
    private String holderName;
    @NotNull(message = "expired date card is blank please try again")
    private Integer expiredDate;
    private Integer pinCode;
    private String type;
}
