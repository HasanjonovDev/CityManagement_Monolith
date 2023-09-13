package uz.pdp.citymanagement_monolith.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.LifecycleState;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;

import java.util.List;

@Entity(name = "role")
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
