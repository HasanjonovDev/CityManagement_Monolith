package uz.pdp.city_management_monolith.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.city_management_monolith.domain.dto.user.UserDto;
import uz.pdp.city_management_monolith.service.user.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/user/api/v1/get")
@RequiredArgsConstructor
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> getUser(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(new ModelMapper().map(userService.getUser(username), UserDto.class))
                .build());
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> getUser(
            @RequestParam UUID id
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(new ModelMapper().map(userService.getUserById(id), UserDto.class))
                .build());
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> details(
            @RequestParam UUID id
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(userService.details(id))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get all user"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @GetMapping("/getAll")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> getAll() {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(userService.getAll())
                .build());
    }
}
