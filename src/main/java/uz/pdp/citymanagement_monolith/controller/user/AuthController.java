package uz.pdp.citymanagement_monolith.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.domain.dto.response.JwtResponse;
import uz.pdp.citymanagement_monolith.domain.dto.user.LoginDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.ResetPasswordDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserRequestDto;
import uz.pdp.citymanagement_monolith.exception.RequestValidationException;
import uz.pdp.citymanagement_monolith.service.user.UserService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/v1/auth")
public class AuthController {
    private final UserService userService;

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Refresh token"
    )
    @GetMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(
            @RequestParam("refreshToken") String refreshToken
    ) {
        return ResponseEntity.ok(userService.refreshToken(refreshToken));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Sign up for the first time"
    )
    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse> signUp(
            @Valid @RequestBody UserRequestDto userCreateDto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) throw new RequestValidationException(bindingResult.getAllErrors());
        return ResponseEntity.ok(userService.signUp(userCreateDto));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Login"
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @RequestBody LoginDto loginDto
    ) {
        return ResponseEntity.ok(userService.login(loginDto));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Reset password link through email"
    )
    @GetMapping("/reset-password/{email}")
    public ResponseEntity<ApiResponse> resetPassword(
            @PathVariable String email
    ) {
        return ResponseEntity.ok(userService.resetPassword(email));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Change the password"
    )
    @PutMapping("/changePassword/{email}/{code}")
    public ResponseEntity<ApiResponse> changePassword(
            @PathVariable String email,
            @RequestBody ResetPasswordDto resetPasswordDto,
            @PathVariable String code
    ) {
        return ResponseEntity.ok(userService.resetPassword(resetPasswordDto, email, code));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Verify the email"
    )
    @GetMapping("/verify/{userId}/{code}")
    public ResponseEntity<ApiResponse> verify(
            @PathVariable String userId,
            @PathVariable String code
    ) {
        return ResponseEntity.ok(userService.verify(UUID.fromString(userId), code));
    }
}
