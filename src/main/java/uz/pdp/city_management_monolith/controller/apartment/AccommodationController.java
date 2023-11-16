package uz.pdp.city_management_monolith.controller.apartment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.city_management_monolith.domain.dto.apartment.AccommodationCreateDto;
import uz.pdp.city_management_monolith.domain.filters.Filter;
import uz.pdp.city_management_monolith.service.apartment.AccommodationService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment/api/v1/accommodation")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Add new apartment with only premium flats"
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ACCOMMODATION_CRUD','PERMISSION_ALL_CRUD')")
    @PostMapping("/add/premium")
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> savePremium(
            @Valid @RequestBody AccommodationCreateDto accommodationCreateDto,
            Principal principal,
            BindingResult bindingResult
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(accommodationService.savePremiumAccommodation
                        (accommodationCreateDto, principal, bindingResult))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Add new apartment with only economy flats"
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ACCOMMODATION_CRUD','PERMISSION_ALL_CRUD')")
    @PostMapping("/add/economy")
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> saveEconomy(
            Principal principal,
            @RequestBody @Valid AccommodationCreateDto accommodationCreateDto,
            BindingResult bindingResult
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(accommodationService.saveEconomyAccommodation
                        (accommodationCreateDto, principal, bindingResult))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get the accommodation by its id"
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/byId/{accommodationId}")
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> getById(
            @PathVariable UUID accommodationId
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(accommodationService.getById(accommodationId))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get all accommodations in the db by its company"
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/byCompany/{companyId}")
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> getByCompany(
            @PathVariable UUID companyId,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(accommodationService.getByCompany(companyId, filter))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get all accommodations in the db"
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/all")
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> getAll(
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(accommodationService.getAll(filter))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get all accommodations by its owner"
    )
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/{userId}")
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> getAllUser(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(accommodationService.getAllByOwner(userId))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "This endpoint is not for ordinary users, it updates accommodation's name by its id"
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ACCOMMODATION_CRUD','PERMISSION_ALL_CRUD','ROLE_ACCOMMODATION_OWNER')")
    @PutMapping("/{newName}/{accommodationId}/update")
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> updateName(
            @PathVariable String newName,
            @PathVariable UUID accommodationId
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(accommodationService.updateName(newName, accommodationId))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "This endpoint is not for ordinary users, it updates accommodation's company by its id"
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ACCOMMODATION_CRUD','PERMISSION_ALL_CRUD','ROLE_ACCOMMODATION_OWNER')")
    @PutMapping("/{companyId}/{accommodationId}/update")
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> updateCompany(
            @PathVariable UUID accommodationId,
            @PathVariable UUID companyId
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(accommodationService.updateCompany(accommodationId, companyId))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "This endpoint is not for ordinary users, it deletes the accommodation by its id"
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ACCOMMODATION_CRUD','PERMISSION_ALL_CRUD','ROLE_ACCOMMODATION_OWNER')")
    @DeleteMapping("/{accommodationId}/delete")
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> delete(
            @PathVariable UUID accommodationId
    ) {
        accommodationService.delete(accommodationId);
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .build());
    }
}
