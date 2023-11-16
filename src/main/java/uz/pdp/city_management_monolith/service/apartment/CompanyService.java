package uz.pdp.city_management_monolith.service.apartment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import uz.pdp.city_management_monolith.domain.dto.apartment.CompanyCreateDto;
import uz.pdp.city_management_monolith.domain.dto.apartment.CompanyForUserDto;
import uz.pdp.city_management_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.city_management_monolith.domain.entity.user.UserEntity;
import uz.pdp.city_management_monolith.domain.filters.Filter;
import uz.pdp.city_management_monolith.exception.DataNotFoundException;
import uz.pdp.city_management_monolith.exception.RequestValidationException;
import uz.pdp.city_management_monolith.repository.apartment.CompanyRepositoryImpl;
import uz.pdp.city_management_monolith.repository.payment.CardRepositoryImpl;
import uz.pdp.city_management_monolith.repository.user.UserRepositoryImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepositoryImpl companyRepository;
    private final UserRepositoryImpl userRepository;
    private final CardRepositoryImpl cardRepository;
    private final ModelMapper modelMapper;

    public CompanyForUserDto save(Principal principal, CompanyCreateDto companyCreateDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) throw new RequestValidationException(bindingResult.getAllErrors());
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        CompanyEntity companyEntity = modelMapper.map(companyCreateDto, CompanyEntity.class);
        companyEntity.setCard(cardRepository.findById(companyCreateDto.getCardId())
                .orElseThrow(() -> new DataNotFoundException("Card not found!")));
        companyEntity.setOwner(user);
        return modelMapper.map(companyRepository.save(companyEntity), CompanyForUserDto.class);
    }


    public CompanyForUserDto get(UUID id) {
        return modelMapper.map(companyRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Company not found!")), CompanyForUserDto.class);
    }

    public List<CompanyForUserDto> getList(UUID id, Filter filter) {
        if(filter == null) filter = new Filter();
        List<CompanyEntity> companyEntitiesByOwnerId = companyRepository.findCompanyEntitiesByOwnerId(id, filter);
        List<CompanyForUserDto> forUser = new ArrayList<>();
        companyEntitiesByOwnerId.forEach((company) -> forUser.add(modelMapper.map(company, CompanyForUserDto.class)));
        return forUser;
    }

    public List<CompanyForUserDto> all(Filter filter) {
        if(filter == null) filter = new Filter();
        List<CompanyEntity> companyEntities = companyRepository.findAll(filter);
        List<CompanyForUserDto> forUser = new ArrayList<>();
        companyEntities.forEach((company) -> forUser.add(modelMapper.map(company, CompanyForUserDto.class)));
        return forUser;
    }
}
