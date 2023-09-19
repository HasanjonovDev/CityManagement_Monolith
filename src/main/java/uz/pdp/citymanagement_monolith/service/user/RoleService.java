package uz.pdp.citymanagement_monolith.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.user.RoleDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.PermissionsForUserDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.RoleForUserDto;
import uz.pdp.citymanagement_monolith.domain.entity.user.RoleEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.user.RoleRepositoryImpl;

import java.util.ArrayList;
import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepositoryImpl roleRepository;
    private final ModelMapper modelMapper;

    public RoleForUserDto save(RoleDto roleDto){
        RoleEntity role = modelMapper.map(roleDto, RoleEntity.class);
        role.setRole("ROLE_" + roleDto.getRole());
        return modelMapper.map(roleRepository.save(role),RoleForUserDto.class);
    }
    public List<RoleForUserDto> getAll(Filter filter){
        List<RoleEntity> all = roleRepository.findAll();
        List<RoleForUserDto> forUsers = new ArrayList<>();
        all.forEach((roleEntity -> forUsers.add(modelMapper.map(roleEntity,RoleForUserDto.class))));
        for (int i=0;i<all.size();i++) {
            List<PermissionsForUserDto> forUserDto = new ArrayList<>();
            all.get(i).getPermissions().forEach((permissionEntity -> forUserDto.add(modelMapper.map(permissionEntity,PermissionsForUserDto.class))));
            forUsers.get(i).setPermissions(forUserDto);
        }
        return forUsers;
    }

    public void deleteById(String name){
        RoleEntity role = roleRepository.findRoleEntityByRole(name)
                .orElseThrow(()->new DataNotFoundException("Role not found"));
        roleRepository.delete(role);
    }

    public RoleForUserDto update(String name ,RoleDto roleDto){
        RoleEntity role = roleRepository.findRoleEntityByRole(name)
                .orElseThrow(()->new DataNotFoundException("Role not found"));
        modelMapper.map(roleDto,role);
        return modelMapper.map(roleRepository.save(role),RoleForUserDto.class);
    }
}
