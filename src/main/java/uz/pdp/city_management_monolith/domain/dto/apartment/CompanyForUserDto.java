package uz.pdp.city_management_monolith.domain.dto.apartment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.city_management_monolith.domain.dto.payment.CardForUserDto;
import uz.pdp.city_management_monolith.domain.dto.user.UserForUserDto;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CompanyForUserDto {
    private UUID id;
    private String name;
    private String description;
    private UserForUserDto owner;
    private CardForUserDto card;
}
