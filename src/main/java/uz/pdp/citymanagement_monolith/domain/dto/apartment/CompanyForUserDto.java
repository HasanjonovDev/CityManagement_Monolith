package uz.pdp.citymanagement_monolith.domain.dto.apartment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CompanyForUserDto {
    private String name;
    private String description;
    private UserEntity owner;
}
