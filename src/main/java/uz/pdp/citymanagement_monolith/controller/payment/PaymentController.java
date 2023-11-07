package uz.pdp.citymanagement_monolith.controller.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.payment.CardDto;
import uz.pdp.citymanagement_monolith.domain.dto.payment.P2PDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.payment.PaymentService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/api/v1")
public class PaymentController {
    private final PaymentService paymentService;

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Do transaction"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/p2p")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> p2p(
            @RequestBody P2PDto p2PDto
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(paymentService.peerToPeer(p2PDto))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Add new card"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/card/save")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> save(
            Principal principal,
            @RequestBody CardDto cardDto
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(paymentService.saveCard(cardDto, principal))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get card by its id"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/card/get/{cardId}")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> getById(
            @PathVariable UUID cardId
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(paymentService.getById(cardId))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get user's all cards"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/card/get")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> get(
            Principal principal,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(paymentService.getCard(principal, filter))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Update card"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('PERMISSION_ALL_CRUD','PERMISSION_CARD_CRUD','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("/card/update/{id}")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> update(
            @PathVariable UUID id,
            @RequestBody CardDto cardDto
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(paymentService.updateCardById(id, cardDto))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Delete card, this action can be done only by admins"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('PERMISSION_ALL_CRUD','PERMISSION_CARD_CRUD','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("/card/delete/{id}")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> delete(
            @PathVariable UUID id
    ) {
        paymentService.deleteCardById(id);
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Fill the balance, this action can be done only by admins"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('PERMISSION_ALL_CRUD','PERMISSION_CARD_CRUD','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("/card/fill/{id}")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> fill(
            @PathVariable UUID id,
            @RequestParam Double balance
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(paymentService.fillBalance(id, balance))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get card by its owner"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/card/{id}")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> getUserCards(
            @PathVariable UUID id,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(paymentService.getCard(id, filter))
                .build());
    }
}
