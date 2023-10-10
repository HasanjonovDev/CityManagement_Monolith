package uz.pdp.citymanagement_monolith.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResultDto {
    private UserDto details;
    private Integer companies;
    private Integer apartments;
    private Integer flats;
}