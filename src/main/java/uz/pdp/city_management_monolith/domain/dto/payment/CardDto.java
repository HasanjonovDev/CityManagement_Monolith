package uz.pdp.city_management_monolith.domain.dto.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

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
    private Date expireDate;
    private Integer pinCode;
    private String type;
}
