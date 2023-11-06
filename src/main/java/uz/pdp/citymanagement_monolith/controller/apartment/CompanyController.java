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
import uz.pdp.citymanagement_monolith.domain.dto.apartment.CompanyCreateDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.apartment.CompanyService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment/api/v1/company")
public class CompanyController {

    private final CompanyService companyService;

    /**
     * @param companyCreateDto is required to configure new object of that is being mapped into an entity
     * @param principal        is required to set the owner of the company to user
     * @param bindingResult    is required to catch any problem in the dto
     * @return saved company
     * @throws uz.pdp.citymanagement_monolith.exception.DataNotFoundException when user is not found and when card is not found
     */
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Add new company"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> add(
            Principal principal,
            @Valid @RequestBody CompanyCreateDto companyCreateDto,
            BindingResult bindingResult
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(companyService.save(principal, companyCreateDto, bindingResult))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get the company by its id"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/{id}")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> get(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(companyService.get(id))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "get the companies by its owner"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}/get")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> getList(
            @PathVariable UUID userId,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(companyService.getList(userId, filter))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get all companies"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> getAll(
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(companyService.all(filter))
                .build());
    }

}
