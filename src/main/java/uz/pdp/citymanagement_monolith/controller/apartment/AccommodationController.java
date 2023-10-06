package uz.pdp.citymanagement_monolith.controller.apartment;

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
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.AccommodationCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.AccommodationForUserDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.apartment.AccommodationService;

import java.security.Principal;
import java.util.List;
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
    public ResponseEntity<AccommodationForUserDto> savePremium(
            @Valid @RequestBody AccommodationCreateDto accommodationCreateDto,
            Principal principal,
            BindingResult bindingResult
    ) {
        return ResponseEntity.ok(accommodationService.savePremiumAccommodation(accommodationCreateDto,principal,bindingResult));
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
    public ResponseEntity<AccommodationForUserDto> saveEconomy(
            Principal principal,
            @RequestPart("accommodationsCreateDto") @Valid AccommodationCreateDto accommodationCreateDto,
            BindingResult bindingResult,
            @RequestPart("image") MultipartFile multipartFile
    ){
        return ResponseEntity.ok(accommodationService.saveEconomyAccommodation
                (accommodationCreateDto,principal, bindingResult,multipartFile));
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
    public ResponseEntity<AccommodationForUserDto> getById(
            @PathVariable UUID accommodationId
    ){
        return ResponseEntity.ok(accommodationService.getById(accommodationId));
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
    public ResponseEntity<List<AccommodationForUserDto>> getByCompany(
            @PathVariable UUID companyId,
            @RequestBody(required = false) Filter filter
    ){
        return ResponseEntity.ok(accommodationService.getByCompany(companyId,filter));
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
    public ResponseEntity<List<AccommodationForUserDto>> getAll(
            @RequestBody(required = false) Filter filter
    ){
        return ResponseEntity.ok(accommodationService.getAll(filter));
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
    public ResponseEntity<AccommodationForUserDto> updateName(
            @PathVariable String newName,
            @PathVariable UUID accommodationId
            ){
        return ResponseEntity.ok(accommodationService.updateName(newName,accommodationId));
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
    public ResponseEntity<AccommodationForUserDto> updateCompany(
            @PathVariable UUID accommodationId,
            @PathVariable UUID companyId
    ){
        return ResponseEntity.ok(accommodationService.updateCompany(accommodationId,companyId));
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
    public ResponseEntity<HttpStatus> delete(
            @PathVariable UUID accommodationId
    ){
        accommodationService.delete(accommodationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
