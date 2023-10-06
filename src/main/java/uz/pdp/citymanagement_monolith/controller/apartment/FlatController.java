package uz.pdp.citymanagement_monolith.controller.apartment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.FlatForUserDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.apartment.FlatService;

import java.security.Principal;
import java.util.List;
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
    @PutMapping ("/update/owner/{userId}/{flatId}")
    public ResponseEntity<FlatForUserDto> setOwner(
            @PathVariable UUID userId,
            @PathVariable UUID flatId
    ){
        return ResponseEntity.ok(flatService.setOwner(userId,flatId));
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
    public ResponseEntity<FlatForUserDto> removeOwner(
            @PathVariable UUID flatId
    ){
        return ResponseEntity.ok(flatService.removeOwner(flatId));
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
    public ResponseEntity<List<FlatForUserDto>> getByAccommodationId(
            @PathVariable UUID id,
            @RequestBody(required = false) Filter filter
    ){
        return ResponseEntity.ok(flatService.getAll(id,filter));
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
    public ResponseEntity<FlatForUserDto> getFlat(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(flatService.getFlatToController(id));
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
    public ResponseEntity<List<FlatForUserDto>> getMine(
            @RequestBody(required = false) Filter filter,
            Principal principal
    ) {
        return ResponseEntity.ok(flatService.getFlat(principal,filter));
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
    public ResponseEntity<List<FlatForUserDto>> getAll(
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(flatService.getAll(filter));
    }
}
