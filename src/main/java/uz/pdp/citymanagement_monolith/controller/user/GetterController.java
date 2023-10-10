package uz.pdp.citymanagement_monolith.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserResultDto;
import uz.pdp.citymanagement_monolith.service.user.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user/api/v1/get")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8085")
public class GetterController {
    private final UserService userService;
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get user by its username"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(new ModelMapper().map(userService.getUser(username), UserDto.class));
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get all doctors"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @GetMapping("/doctors")
    public ResponseEntity<List<UserDto>> getDoctors(){
        return ResponseEntity.ok(userService.getDoctors());
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get the user bu its id"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @GetMapping("/id")
    public ResponseEntity<UserDto> getUser(
            @RequestParam UUID id
    ) {
        return ResponseEntity.ok(new ModelMapper().map(userService.getUserById(id), UserDto.class));
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get the user details bu its id"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @GetMapping("/details")
    public ResponseEntity<UserResultDto> details(
            @RequestParam UUID id
    ) {
        return ResponseEntity.ok(userService.details(id));
    }

}
