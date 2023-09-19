package uz.pdp.citymanagement_monolith.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/save")
    public ResponseEntity<RoleForUserDto>saveRole(
            @RequestBody RoleDto roleDto
    ){
        return ResponseEntity.ok(roleService.save(roleDto));
    }

    @GetMapping("/getRole")
    public ResponseEntity<List<RoleForUserDto>> getAllRole(
            @RequestBody Filter filter
    ){
        return ResponseEntity.ok(roleService.getAll(filter));
    }

    @DeleteMapping("/{name}/delete")
    public ResponseEntity<HttpStatus>deleteRole(
            @PathVariable String name
    ){
        roleService.deleteById(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{name}/update")
    public ResponseEntity<RoleForUserDto>updateRole(
            @RequestBody RoleDto roleDto,
            @PathVariable String name
    ){
        return ResponseEntity.ok(roleService.update(name,roleDto));
    }
}
