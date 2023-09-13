package uz.pdp.citymanagement_monolith.domain.entity.user;

import jakarta.persistence.Entity;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;

@Entity(name = "permissions")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PermissionEntity extends BaseEntity {
    private String permission;
}
