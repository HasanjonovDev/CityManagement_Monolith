package uz.pdp.city_management_monolith.domain.entity.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.pdp.city_management_monolith.domain.entity.BaseEntity;

@Entity(name = "verification")
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VerificationEntity extends BaseEntity {
    @ManyToOne
    UserEntity user;
    Long code;
}
