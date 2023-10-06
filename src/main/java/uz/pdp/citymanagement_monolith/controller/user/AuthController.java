package uz.pdp.citymanagement_monolith.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.user.LoginDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.ResetPasswordDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserRequestDto;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.domain.dto.response.JwtResponse;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserDto;
import uz.pdp.citymanagement_monolith.exception.RequestValidationException;
import uz.pdp.citymanagement_monolith.service.user.UserService;


import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/v1/auth")
@CrossOrigin(origins = "http://localhost:8085")
public class AuthController {
    private final UserService userService;
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Sign up for the first time"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(
            @Valid @RequestBody UserRequestDto userCreateDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) throw new RequestValidationException(bindingResult.getAllErrors());
        return ResponseEntity.ok(userService.signUp(userCreateDto));
    }
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Login"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody LoginDto loginDto
    ) {
        return ResponseEntity.ok(userService.login(loginDto));
    }
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Reset password link through email"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @GetMapping("/reset-password/{email}")
    public ResponseEntity<ApiResponse> resetPassword(
            @PathVariable String email
    ){
        return ResponseEntity.ok(userService.resetPassword(email));
    }
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Change the password"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PutMapping("/changePassword/{email}")
    public ResponseEntity<ApiResponse> changePassword(
            @PathVariable String email,
            @RequestBody ResetPasswordDto resetPasswordDto
    ) {
        return ResponseEntity.ok(userService.resetPassword(resetPasswordDto,email));
    }
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Verify the email"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @GetMapping ("/verify/{userId}/{code}")
    public ResponseEntity<ApiResponse> verify(
            @PathVariable String userId,
            @PathVariable String code
    ){
        return ResponseEntity.ok(userService.verify(UUID.fromString(userId),code));
    }
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Change user's name"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/changeName/{name}")
    public ResponseEntity<ApiResponse> changeName(
            Principal principal,
            @PathVariable String name
    ){
        return ResponseEntity.ok(userService.changeName(principal,name));
    }

}
