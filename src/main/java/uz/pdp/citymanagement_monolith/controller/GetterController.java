package uz.pdp.citymanagement_monolith.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.entity.UserEntity;
import uz.pdp.citymanagement_monolith.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/api/v1/get")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8085")
@PreAuthorize("permitAll()")
public class GetterController {
    private final UserService userService;
    @GetMapping("/user")
    public ResponseEntity<UserEntity> getUser(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<UserEntity>> getDoctors(){
        return ResponseEntity.ok(userService.getDoctors());
    }

    @GetMapping("/id")
    public ResponseEntity<UserEntity> getUser(
            @RequestParam UUID id
    ) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
