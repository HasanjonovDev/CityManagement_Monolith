package uz.pdp.citymanagement_monolith.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.user.RoleDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.RoleForUserDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.user.RoleService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user/api/v1/role")
public class RoleController {
    private final RoleService roleService;
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ROLE_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<RoleForUserDto>saveRole(
            @RequestBody RoleDto roleDto
    ){
        return ResponseEntity.ok(roleService.save(roleDto));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ROLE_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN')")
    @GetMapping("/getRole")
    public ResponseEntity<List<RoleForUserDto>> getAllRole(
            @RequestBody Filter filter
    ){
        return ResponseEntity.ok(roleService.getAll(filter));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ROLE_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN')")
    @DeleteMapping("/{name}/delete")
    public ResponseEntity<HttpStatus>deleteRole(
            @PathVariable String name
    ){
        roleService.deleteById(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_ROLE_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN')")
    @PutMapping("/{name}/update")
    public ResponseEntity<RoleForUserDto>updateRole(
            @RequestBody RoleDto roleDto,
            @PathVariable String name
    ){
        return ResponseEntity.ok(roleService.update(name,roleDto));
    }
}
