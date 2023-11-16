package uz.pdp.city_management_monolith.domain.dto.reader;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class VerificationDto {
    private UUID userId;
    private String code;
}
