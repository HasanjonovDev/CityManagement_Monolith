package uz.pdp.citymanagement_monolith.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class RoleEntity {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    String role;
}
