package uz.pdp.citymanagement_monolith.service.apartment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.CompanyCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.CompanyForUserDto;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.exception.RequestValidationException;
import uz.pdp.citymanagement_monolith.repository.apartment.CompanyRepositoryImpl;
import uz.pdp.citymanagement_monolith.service.user.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepositoryImpl companyRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CompanyForUserDto save(Principal principal, CompanyCreateDto companyCreateDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new RequestValidationException(bindingResult.getAllErrors());
        }
        UserEntity user = userService.getUser(principal.getName());
        CompanyEntity companyEntity = modelMapper.map(companyCreateDto, CompanyEntity.class);

        companyEntity.setOwner(user);
        return modelMapper.map(companyRepository.save(companyEntity), CompanyForUserDto.class);
    }


    public CompanyForUserDto get(UUID id) {
        return modelMapper.map(companyRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Company not found!")), CompanyForUserDto.class);
    }

    public List<CompanyForUserDto> getList(UUID id, Filter filter) {
        List<CompanyEntity> companyEntitiesByOwnerId = companyRepository.findCompanyEntitiesByOwnerId(id, filter);
        List<CompanyForUserDto> forUser = new ArrayList<>();
        companyEntitiesByOwnerId.forEach((company) -> forUser.add(modelMapper.map(company, CompanyForUserDto.class)));
        return forUser;
    }
}
