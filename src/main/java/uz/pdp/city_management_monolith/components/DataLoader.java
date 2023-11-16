/*
package uz.pdp.citymanagement_monolith.components;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.RoleEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserState;
import uz.pdp.citymanagement_monolith.repository.user.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlMode;

    @Override
    public void run(String... args) {
        if (Objects.equals(ddlMode, "update")) {
            if (userRepository.findUserEntityByEmail("admin@gmail.com").isEmpty()) {
                userRepository.save(new UserEntity(
                        "admin",
                        "admin",
                        "admin@gmail.com",
                        new Date(),
                        "$2a$12$o2rNrQA1lIo7ztraDD0zGu3599e9rX.LDdyIws7sZWEiEVzUi3Iqa",
                        List.of(new RoleEntity("ROLE_SUPER_ADMIN", List.of(new PermissionEntity("PERMISSION_ALL_CRUD")))),
                        UserState.ACTIVE,
                        12,
                        "12332"
                ));
            }
            RoleEntity UserRole = RoleEntity.builder()
                    .role("ROLE_USER")
                    .permissions(List.of(PermissionEntity.builder()
                            .permission("SIGN_IN")
                            .build(), PermissionEntity.builder()
                            .permission("SIGN_UP")
                            .build()))
                    .build();
        }
    }
}
*/
