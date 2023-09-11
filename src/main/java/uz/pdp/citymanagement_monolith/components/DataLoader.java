package uz.pdp.citymanagement_monolith.components;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.citymanagement_monolith.domain.entity.RoleEntity;
import uz.pdp.citymanagement_monolith.domain.entity.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.UserState;
import uz.pdp.citymanagement_monolith.repository.RoleRepository;
import uz.pdp.citymanagement_monolith.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlMode;

    @Override
    public void run(String... args) {
        RoleEntity roleEntity = new RoleEntity("SUPER_ADMIN");
        if(roleRepository.findById(roleEntity.getRole()).isPresent()) {
            roleEntity = roleRepository.findById(roleEntity.getRole()).get();
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
        }
    }
}
