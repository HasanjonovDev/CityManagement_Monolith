package uz.pdp.citymanagement_monolith.service.apartment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.AccommodationCreateDto;
import uz.pdp.citymanagement_monolith.domain.entity.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.*;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.exception.RequestValidationException;
import uz.pdp.citymanagement_monolith.repository.apartment.AccommodationRepository;
import uz.pdp.citymanagement_monolith.repository.apartment.CompanyRepository;
import uz.pdp.citymanagement_monolith.repository.apartment.FlatRepository;
import uz.pdp.citymanagement_monolith.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final CompanyRepository companyRepository;
    private final FlatRepository flatRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public AccommodationEntity savePremiumAccommodation(AccommodationCreateDto accommodationCreateDto, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }

        UserEntity user = userService.getUser(principal.getName());
        AccommodationEntity accommodation = modelMapper.map(accommodationCreateDto, AccommodationEntity.class);

        CompanyEntity companyEntity = companyRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new DataNotFoundException("Company Not Found!"));

        List<FlatEntity> flats = new ArrayList<>();
        int number = 1;
        int floor = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                FlatEntity premiumFlat = FlatEntity.builder()
                        .about("Premium flat which includes 6 rooms. LivingRoom,Bedroom,ChildrenRoom,DiningRoom,Kitchen,Bathroom. 800 USD/month")
                        .rooms(6)
                        .number(number++)
                        .pricePerMonth(800.0)
                        .FullPrice(100000.0)
                        .whichFloor(floor)
                        .flatType(FlatType.PREMIUM)
                        .ownerId(companyEntity.getOwnerId())
                        .company(companyEntity)
                        .status(FlatStatus.AVAILABLE)
                        .build();
                flatRepository.save(premiumFlat);
                flats.add(premiumFlat);
            }
            floor++;
        }
        accommodation.setFlats(flats);
        accommodation.setFloors(4);
        accommodation.setCompany(companyEntity);
        accommodation.setNumberOfFlats(8);
        accommodation.setLocationEntity(accommodationCreateDto.getLocationEntity());
        accommodation.setName(accommodationCreateDto.getName());

        return accommodationRepository.save(accommodation);

    }

    public AccommodationEntity saveEconomyAccommodation(AccommodationCreateDto accommodationCreateDto,Principal principal,BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            throw new RequestValidationException(bindingResult.getAllErrors());
        }

        UserEntity user = userService.getUser(principal.getName());
        AccommodationEntity accommodation = modelMapper.map(accommodationCreateDto, AccommodationEntity.class);

        CompanyEntity companyEntity = companyRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new DataNotFoundException("Company Not Found!"));
        
        List<FlatEntity> flats = new ArrayList<>();
        int number = 1;
        int floor = 1;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 4; j++) {
                FlatEntity premiumFlat = FlatEntity.builder()
                        .about("Economy flat which includes 4 rooms. Bedroom,ChildrenRoom,Kitchen,Bathroom. 500 USD/month")
                        .rooms(4)
                        .number(number++)
                        .pricePerMonth(500.0)
                        .FullPrice(60000.0)
                        .whichFloor(floor)
                        .flatType(FlatType.ECONOMY)
                        .ownerId(companyEntity.getId())
                        .company(companyEntity)
                        .status(FlatStatus.AVAILABLE)
                        .build();
                flatRepository.save(premiumFlat);
                flats.add(premiumFlat);
            }
            floor++;
        }
        accommodation.setFlats(flats);
        accommodation.setFloors(9);
        accommodation.setCompany(companyEntity);
        accommodation.setNumberOfFlats(36);
        accommodation.setLocationEntity(accommodationCreateDto.getLocationEntity());
        accommodation.setName(accommodationCreateDto.getName());

        return accommodationRepository.save(accommodation);
    }

    public AccommodationEntity getById(UUID accommodationId){
        return accommodationRepository.findById(accommodationId)
                .orElseThrow(()-> new DataNotFoundException("Accommodation Not Found!"));
    }

    public List<AccommodationEntity> getByCompany(UUID companyId){
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new DataNotFoundException("Company Not Found!"));
        return accommodationRepository.findByCompany(company);
    }

    public List<AccommodationEntity> getAll(){
        return accommodationRepository.findAll();
    }

    public AccommodationEntity updateName(String newName, UUID accommodationId){
        AccommodationEntity accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new DataNotFoundException("Accommodation Not Found!"));
        accommodation.setName(newName);
        return accommodationRepository.save(accommodation);
    }

    public AccommodationEntity updateCompany(UUID accommodationId,String companyName){
        AccommodationEntity accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new DataNotFoundException("Accommodation Not Found!"));

        CompanyEntity company = companyRepository.findByName(companyName)
                .orElseThrow(() -> new DataNotFoundException("Company Not Found!"));

        accommodation.setCompany(company);
        return accommodationRepository.save(accommodation);
    }

    public void delete(UUID accommodationId){
        AccommodationEntity accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new DataNotFoundException("Accommodation Not Found!"));
        accommodationRepository.delete(accommodation);
    }
}
