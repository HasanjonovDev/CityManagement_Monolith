package uz.pdp.city_management_monolith.domain.dto.booking;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PreOrderRequestDto {
    private UUID flatId;
    private Integer days;
    private Date date;
}
