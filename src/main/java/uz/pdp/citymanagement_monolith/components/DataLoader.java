/*
package uz.pdp.citymanagement_monolith.components;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.RoleEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserState;
import uz.pdp.citymanagement_monolith.repository.user.PermissionRepository;
import uz.pdp.citymanagement_monolith.repository.user.RoleRepository;
import uz.pdp.citymanagement_monolith.repository.user.UserRepository;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlMode;

    @Override
    public void run(String... args) {
        RoleEntity roleEntity = new RoleEntity("ROLE_SUPER_ADMIN",permissionRepository.findAll());
        if(roleRepository.findRoleEntityByRole(roleEntity.getRole()).isPresent()) {
            roleEntity = roleRepository.findRoleEntityByRole(roleEntity.getRole()).get();
        }
        if (Objects.equals(ddlMode, "update")) {
            if (userRepository.findUserEntityByEmail("admin@gmail.com").isEmpty()) {
                userRepository.save(new UserEntity(
                        "admin",
                        "admin@gmail.com",
                        passwordEncoder.encode("admin"),
                        List.of(roleEntity),
                        UserState.ACTIVE,
                        0
                ));
            }
            RoleEntity UserRole = RoleEntity.builder()
                    .role("ROLE_USER")
                    .permissions(List.of(PermissionEntity.builder()
                            .permission("SIGN_IN")
                            .build(),PermissionEntity.builder()
                            .permission("SIGN_UP")
                            .build()))
                    .build();
            if (roleRepository.findRoleEntityByRole(UserRole.getRole()).isEmpty()){
                roleRepository.save(UserRole);
            }
        }
    }
}
*/
