package uz.pdp.citymanagement_monolith.domain.dto.booking;

import lombok.*;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserForUserDto;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PreOrderBookingForUserDto {
    private Date toWhichDate;
    private int numberOfDays;
    private Double prePayAmount;
    private UserForUserDto owed;
}
