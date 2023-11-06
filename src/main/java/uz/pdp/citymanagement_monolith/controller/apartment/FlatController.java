package uz.pdp.citymanagement_monolith.controller.apartment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.apartment.FlatService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment/api/v1/flat")
public class FlatController {
    private final FlatService flatService;

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Set new owner to a flat by their ids"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_FLAT_CRUD','PERMISSION_ALL_CRUD')")
    @PutMapping("/update/owner/{userId}/{flatId}")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> setOwner(
            @PathVariable UUID userId,
            @PathVariable UUID flatId
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(flatService.setOwner(userId, flatId))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Remove owner and set company owner instead"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_FLAT_CRUD','PERMISSION_ALL_CRUD')")
    @PutMapping("/remove/owner/{flatId}")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> removeOwner(
            @PathVariable UUID flatId
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(flatService.removeOwner(flatId))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get accommodation's all flats"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/accommodation")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> getByAccommodationId(
            @PathVariable UUID id,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(flatService.getAll(id, filter))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get flat by its id"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/{id}")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> getFlat(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(flatService.getFlatToController(id))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get user's all flats by principal"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/gt")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> getMine(
            @RequestBody(required = false) Filter filter,
            Principal principal
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(flatService.getFlat(principal, filter))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get all flats in the db"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/all")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> getAll(
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(flatService.getAll(filter))
                .build());
    }
}
