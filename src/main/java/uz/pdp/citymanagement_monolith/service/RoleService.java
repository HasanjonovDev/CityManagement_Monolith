package uz.pdp.citymanagement_monolith.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.RoleDto;
import uz.pdp.citymanagement_monolith.domain.entity.RoleEntity;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.RoleRepository;

import java.util.List;


@Transactional
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    public RoleEntity save(RoleDto roleDto){
        RoleEntity role = modelMapper.map(roleDto, RoleEntity.class);
        role.setRole("ROLE_" + roleDto.getRole());
        return roleRepository.save(role);
    }



    public List<RoleEntity> getAll(){
        return roleRepository.findAll();
    }

    public void deleteById(String name){
        RoleEntity role = roleRepository.findById(name)
                .orElseThrow(()->new DataNotFoundException("Role not found"));
        roleRepository.delete(role);
    }

    public RoleEntity update(String name ,RoleDto roleDto){
        RoleEntity role = roleRepository.findById(name)
                .orElseThrow(()->new DataNotFoundException("Role not found"));
        modelMapper.map(roleDto,role);
        return roleRepository.save(role);
    }
}
