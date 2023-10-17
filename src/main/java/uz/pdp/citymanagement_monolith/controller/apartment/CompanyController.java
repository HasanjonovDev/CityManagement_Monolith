package uz.pdp.citymanagement_monolith.controller.apartment;


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
import uz.pdp.citymanagement_monolith.domain.dto.apartment.CompanyCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.CompanyForUserDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.apartment.CompanyService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment/api/v1/company")
public class CompanyController {

    private final CompanyService companyService;
    /**
     * @param companyCreateDto is required to configure new object of that is being mapped into an entity
     * @param principal is required to set the owner of the company to user
     * @param bindingResult is required to catch any problem in the dto
     * @throws uz.pdp.citymanagement_monolith.exception.DataNotFoundException when user is not found and when card is not found
     * @return saved company
     * */
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
    public ResponseEntity<CompanyForUserDto> add(
            Principal principal,
            @Valid @RequestBody CompanyCreateDto companyCreateDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(companyService.save(principal,companyCreateDto,bindingResult));
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
    public ResponseEntity<CompanyForUserDto> get(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(companyService.get(id));
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
    public ResponseEntity<List<CompanyForUserDto>> getList(
            @PathVariable UUID userId,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(companyService.getList(userId,filter));
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
    public ResponseEntity<List<CompanyForUserDto>> getAll(
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(companyService.all(filter));
    }

}
