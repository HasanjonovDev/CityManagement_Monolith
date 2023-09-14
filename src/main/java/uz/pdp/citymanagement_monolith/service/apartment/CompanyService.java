package uz.pdp.citymanagement_monolith.service.apartment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.CompanyCreateDto;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.exception.RequestValidationException;
import uz.pdp.citymanagement_monolith.repository.apartment.CompanyRepository;
import uz.pdp.citymanagement_monolith.service.user.UserService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CompanyEntity save(Principal principal, CompanyCreateDto companyCreateDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new RequestValidationException(bindingResult.getAllErrors());
        }
        UserEntity user = userService.getUser(principal.getName());
        CompanyEntity companyEntity = modelMapper.map(companyCreateDto, CompanyEntity.class);

        companyEntity.setOwnerId(user.getId());
        return companyRepository.save(companyEntity);
    }


    public CompanyEntity get(UUID id) {
        return companyRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Company not found!"));
    }

    public List<CompanyEntity> getList(UUID id) {
        return companyRepository.findCompanyEntitiesByOwnerId(id);
    }
}
