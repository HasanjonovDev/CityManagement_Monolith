package uz.pdp.citymanagement_monolith.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.citymanagement_monolith.service.user.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/user/api/v1/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Block user"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @RequestMapping(value = "/block/{userId}",method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','PERMISSION_USER_CRUD')")
    public ResponseEntity<HttpStatus> blockUser(
            @PathVariable UUID userId
    ) {
        userService.block(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Unblock user"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @RequestMapping(value = "/unblock/{userId}",method = RequestMethod.PUT)
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','PERMISSION_USER_CRUD')")
    public ResponseEntity<HttpStatus> unblockUser(
            @PathVariable UUID userId
    ) {
        userService.unblock(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
