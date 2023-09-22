package uz.pdp.citymanagement_monolith.controller.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserDto;
import uz.pdp.citymanagement_monolith.service.user.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/api/v1/get")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8085")
public class GetterController {
    private final UserService userService;
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(new ModelMapper().map(userService.getUser(username), UserDto.class));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<UserDto>> getDoctors(){
        return ResponseEntity.ok(userService.getDoctors());
    }

    @GetMapping("/id")
    public ResponseEntity<UserDto> getUser(
            @RequestParam UUID id
    ) {
        return ResponseEntity.ok(new ModelMapper().map(userService.getUserById(id), UserDto.class));
    }
}
