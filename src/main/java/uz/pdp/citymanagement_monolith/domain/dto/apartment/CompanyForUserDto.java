package uz.pdp.citymanagement_monolith.domain.dto.apartment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.citymanagement_monolith.domain.dto.payment.CardForUserDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserForUserDto;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CompanyForUserDto {
    private String name;
    private String description;
    private UserForUserDto owner;
    private CardForUserDto card;
}
