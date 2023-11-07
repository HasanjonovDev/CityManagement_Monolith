package uz.pdp.citymanagement_monolith.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.domain.dto.user.EditPermissionDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.PermissionCreateDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.RequestValidationException;
import uz.pdp.citymanagement_monolith.service.user.PermissionService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions/api/v1")
public class PermissionController {
    private final PermissionService permissionService;

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Add new permission to a role"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_PERMISSION_CRUD','PERMISSION_ALL_CRUD')")
    @PostMapping("/add/{roleId}")
    public ResponseEntity<ApiResponse> add(
            @Valid @RequestBody PermissionCreateDto permissionCreateDto,
            BindingResult bindingResult,
            @PathVariable UUID roleId
    ) {
        if (bindingResult.hasErrors()) throw new RequestValidationException(bindingResult.getAllErrors());
        return ResponseEntity.ok(permissionService.addPermission(permissionCreateDto, roleId));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get role by its id"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_PERMISSION_CRUD','PERMISSION_ALL_CRUD')")
    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable UUID roleId,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(permissionService.get(roleId, filter));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Delete role"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_PERMISSION_CRUD','PERMISSION_ALL_CRUD')")
    @DeleteMapping("/del/{id}")
    public ResponseEntity<ApiResponse> del(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(permissionService.delete(id));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Update role"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_PERMISSION_CRUD','PERMISSION_ALL_CRUD')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable UUID id,
            @RequestBody EditPermissionDto editPermissionDto
    ) {
        return ResponseEntity.ok(permissionService.update(editPermissionDto, id));
    }
}
