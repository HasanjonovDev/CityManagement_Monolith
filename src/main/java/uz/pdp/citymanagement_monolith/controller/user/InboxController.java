package uz.pdp.citymanagement_monolith.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserInboxCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserInboxForUserDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.ExceptionFront;
import uz.pdp.citymanagement_monolith.service.user.UserInboxService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/v1/inbox")
public class InboxController {
    private final UserInboxService userInboxService;
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Add new message"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN','PERMISSION_ALL_CRUD','PERMISSION_INBOX_CRUD')")
    @RequestMapping(value = "/add/message/{userId}")
    public ResponseEntity<UserInboxForUserDto> addNewMessage(
            @Valid @RequestBody UserInboxCreateDto userInboxCreateDto,
            @PathVariable UUID userId,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) throw new ExceptionFront(bindingResult.getAllErrors());
        return ResponseEntity.ok(userInboxService.addNewMessage(userId,userInboxCreateDto));
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get inbox messages"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/get/{userId}")
    public ResponseEntity<List<UserInboxForUserDto>> getAllUserInbox(
            @PathVariable UUID userId,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(userInboxService.getAllUserInbox(userId,filter));
    }
}
