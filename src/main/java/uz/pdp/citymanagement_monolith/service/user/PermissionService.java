package uz.pdp.citymanagement_monolith.service.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.domain.dto.user.EditPermissionDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.PermissionCreateDto;
import uz.pdp.citymanagement_monolith.domain.entity.user.PermissionEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.RoleEntity;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.user.PermissionRepository;
import uz.pdp.citymanagement_monolith.repository.user.RoleRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    public ApiResponse addPermission(PermissionCreateDto permissionCreateDto, UUID roleId) {
        RoleEntity roleEntity = roleRepository.findById(roleId).orElseThrow(() -> new DataNotFoundException("Role not found!"));
        try {
            PermissionEntity map = modelMapper.map(permissionCreateDto, PermissionEntity.class);
            PermissionEntity save = permissionRepository.save(map);
            roleEntity.getPermissions().add(save);
            roleRepository.save(roleEntity);
            return new ApiResponse(HttpStatus.OK,true,"Successfully added!");
        } catch (Exception e) {
            return new ApiResponse(HttpStatus.BAD_REQUEST,false,e.getMessage());
        }
    }

    public ApiResponse get(UUID roleId) {
        List<PermissionEntity> permissions = roleRepository.permissions(roleId);
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .message("OK")
                .success(true)
                .data(permissions)
                .build();
    }
    public ApiResponse delete(UUID id) {
//        PermissionEntity permissionEntity = permissionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Permission not found!"));
        permissionRepository.deleteById(id);
        return ApiResponse.builder()
                .message("OK")
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }

    public ApiResponse update(EditPermissionDto editPermissionDto, UUID id) {
        PermissionEntity permissionEntity = permissionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Permission not found!"));
        modelMapper.map(editPermissionDto,permissionEntity);
        return ApiResponse.builder()
                .success(true)
                .message("OK")
                .status(HttpStatus.OK)
                .build();
    }
}
