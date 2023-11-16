package uz.pdp.city_management_monolith.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.city_management_monolith.domain.dto.user.RoleDto;
import uz.pdp.city_management_monolith.domain.filters.Filter;
import uz.pdp.city_management_monolith.service.user.RoleService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/v1/role")
public class RoleController {
    private final RoleService roleService;

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Add new role"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ROLE_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> saveRole(
            @RequestBody RoleDto roleDto
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(roleService.save(roleDto))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get all roles"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ROLE_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN')")
    @GetMapping("/getRole")
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> getAllRole(
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(roleService.getAll(filter))
                .build());
    }

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Delete role"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ROLE_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{name}/delete")
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> deleteRole(
            @PathVariable String name
    ) {
        roleService.deleteById(name);
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
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
            description = "Update role"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ROLE_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN')")
    @PutMapping("/{name}/update")
    public ResponseEntity<uz.pdp.city_management_monolith.domain.dto.response.ApiResponse> updateRole(
            @RequestBody RoleDto roleDto,
            @PathVariable String name
    ) {
        return ResponseEntity.ok(uz.pdp.city_management_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(roleService.update(name, roleDto))
                .build());
    }
}
