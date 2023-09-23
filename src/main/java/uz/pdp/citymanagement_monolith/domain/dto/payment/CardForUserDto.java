package uz.pdp.citymanagement_monolith.domain.dto.payment;

import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardType;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CardForUserDto {
    private UUID id;
    private LocalDateTime createdDate;
    private String number;
    private String holderName;
    private Integer month;
    private Integer year;
    private Double balance;
    private UUID ownerId;
    private CardType type;
}
