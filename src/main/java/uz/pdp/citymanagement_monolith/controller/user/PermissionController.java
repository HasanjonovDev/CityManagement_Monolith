package uz.pdp.citymanagement_monolith.controller.user;

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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_PERMISSION_CRUD','PERMISSION_ALL_CRUD')")
    @PostMapping("/add/{roleId}")
    public ResponseEntity<ApiResponse> add(
            @Valid @RequestBody PermissionCreateDto permissionCreateDto,
            BindingResult bindingResult,
            @PathVariable UUID roleId
    ) {
        if(bindingResult.hasErrors()) throw new RequestValidationException(bindingResult.getAllErrors());
        return ResponseEntity.ok(permissionService.addPermission(permissionCreateDto,roleId));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_PERMISSION_CRUD','PERMISSION_ALL_CRUD')")
    @GetMapping("/{roleId}")
    public ResponseEntity<ApiResponse> getById(
            @PathVariable UUID roleId,
            @RequestBody Filter filter
    ) {
        return ResponseEntity.ok(permissionService.get(roleId,filter));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_PERMISSION_CRUD','PERMISSION_ALL_CRUD')")
    @DeleteMapping("/del/{id}")
    public ResponseEntity<ApiResponse> del(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(permissionService.delete(id));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_PERMISSION_CRUD','PERMISSION_ALL_CRUD')")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> update(
            @PathVariable UUID id,
            @RequestBody EditPermissionDto editPermissionDto
    ){
        return ResponseEntity.ok(permissionService.update(editPermissionDto,id));
    }
}
