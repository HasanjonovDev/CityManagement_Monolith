package uz.pdp.citymanagement_monolith.domain.dto.payment;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class P2PDto {
    private String sender;
    private String receiver;
    private Double cash;
}
