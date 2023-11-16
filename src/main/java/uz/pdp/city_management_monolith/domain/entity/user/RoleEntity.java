package uz.pdp.city_management_monolith.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.pdp.city_management_monolith.domain.entity.BaseEntity;

import java.util.List;

@Entity(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RoleEntity extends BaseEntity {
    @Column(unique = true)
    String role;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    List<PermissionEntity> permissions;
}
