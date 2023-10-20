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
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserInboxCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserInboxForUserDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.ExceptionFront;
import uz.pdp.citymanagement_monolith.service.user.UserInboxService;

import java.security.Principal;
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
    @RequestMapping(value = "/add/message/{userId}",method = RequestMethod.POST)
    public ResponseEntity<UserInboxForUserDto> addNewMessage(
            @Valid @RequestBody UserInboxCreateDto userInboxCreateDto,
            @PathVariable UUID userId,
            BindingResult bindingResult,
            Principal principal
    ) {
        if(bindingResult.hasErrors()) throw new ExceptionFront(bindingResult.getAllErrors());
        return ResponseEntity.ok(userInboxService.addNewMessage(userId,userInboxCreateDto,principal));
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get inbox messages by user id"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/get/{userId}",method = RequestMethod.GET)
    public ResponseEntity<List<UserInboxForUserDto>> getAllUserInbox(
            @PathVariable UUID userId,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(userInboxService.getAllUserInbox(userId,filter));
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get inbox by its id"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{inboxId}/get",method = RequestMethod.GET)
    public ResponseEntity<UserInboxForUserDto> getInboxDetails(
            @PathVariable UUID inboxId
    ) {
        return ResponseEntity.ok(userInboxService.getInbox(inboxId));
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Approve the booking"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{inboxId}/approve",method = RequestMethod.PUT)
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> approve(
            @PathVariable UUID inboxId
    ) {
        return ResponseEntity.ok(userInboxService.approveBooking(inboxId));
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Reject the booking"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{inboxId}/reject",method = RequestMethod.PUT)
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> reject(
            @PathVariable UUID inboxId
    ) {
        return ResponseEntity.ok(userInboxService.rejectBooking(inboxId));
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Approve the booking"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/purchase/{inboxId}/approve",method = RequestMethod.PUT)
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> approvePurchase(
            @PathVariable UUID inboxId,
            @RequestParam(name = "cardNumber") String cardNumber
    ) {
        return ResponseEntity.ok(userInboxService.approveBuy(inboxId,cardNumber));
    }
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Reject the booking"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/purchase/{inboxId}/reject",method = RequestMethod.PUT)
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> rejectPurchase(
            @PathVariable UUID inboxId
    ) {
        return ResponseEntity.ok(userInboxService.rejectBuy(inboxId));
    }
}
