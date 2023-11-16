package uz.pdp.city_management_monolith.domain.dto.payment;

import lombok.*;
import uz.pdp.city_management_monolith.domain.dto.user.UserDto;
import uz.pdp.city_management_monolith.domain.entity.payment.CardType;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CardForUserDto {
    private UUID id;
    private Date createdDate;
    private String number;
    private String holderName;
    private Date expiredDate;
    private Double balance;
    private UserDto owner;
    private CardType type;
}
