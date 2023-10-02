package uz.pdp.citymanagement_monolith.service.apartment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.AccommodationCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.AccommodationForUserDto;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.*;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.exception.RequestValidationException;
import uz.pdp.citymanagement_monolith.repository.apartment.AccommodationRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.apartment.CompanyRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.apartment.FlatRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserRepositoryImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccommodationService {
    private final AccommodationRepositoryImpl accommodationRepository;
    private final CompanyRepositoryImpl companyRepository;
    private final UserRepositoryImpl userRepository;
    private final FlatRepositoryImpl flatRepository;
    private final ModelMapper modelMapper;
    @Value("${images.path}")
    private String imagesPath;
    public AccommodationForUserDto savePremiumAccommodation(AccommodationCreateDto accommodationCreateDto, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }

        UserEntity user = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));

        CompanyEntity companyEntity = companyRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new DataNotFoundException("Company Not Found!"));

        AccommodationEntity accommodation = modelMapper.map(accommodationCreateDto, AccommodationEntity.class);
        accommodation.setFloors(4);
        accommodation.setCompany(companyEntity);
        accommodation.setNumberOfFlats(8);
        accommodation.setLocationEntity(accommodationCreateDto.getLocationEntity());
        accommodation.setName(accommodationCreateDto.getName());
        AccommodationEntity savedAccommodation = accommodationRepository.save(accommodation);

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
                        .owner(companyEntity.getOwner())
                        .status(FlatStatus.AVAILABLE)
                        .accommodation(accommodation)
                        .build();
                flatRepository.save(premiumFlat);
            }
            floor++;
        }
        return modelMapper.map(savedAccommodation,AccommodationForUserDto.class);

    }

    public AccommodationForUserDto saveEconomyAccommodation(AccommodationCreateDto accommodationCreateDto, Principal principal, BindingResult bindingResult, MultipartFile file){
        if (bindingResult.hasErrors()){
            throw new RequestValidationException(bindingResult.getAllErrors());
        }

        UserEntity user = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        CompanyEntity companyEntity = companyRepository.findByOwnerId(user.getId())
                .orElseThrow(() -> new DataNotFoundException("Company Not Found!"));
        AccommodationEntity accommodation = modelMapper.map(accommodationCreateDto, AccommodationEntity.class);
        accommodation.setFloors(9);
        accommodation.setCompany(companyEntity);
        accommodation.setNumberOfFlats(36);
        accommodation.setLocationEntity(accommodationCreateDto.getLocationEntity());
        accommodation.setName(accommodationCreateDto.getName());
        String s = saveImage(file);
        accommodation.setImgPath(s);
        AccommodationEntity savedAccommodation = accommodationRepository.save(accommodation);

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
                        .owner(companyEntity.getOwner())
                        .accommodation(accommodation)
                        .status(FlatStatus.AVAILABLE)
                        .build();
                flatRepository.save(premiumFlat);
            }
            floor++;
        }
        return modelMapper.map(savedAccommodation,AccommodationForUserDto.class);
    }

    private String saveImage(MultipartFile file) {
        File directory = new File(imagesPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory and any necessary parent directories
        }
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;

        // Construct the file path
        Path filePath = Paths.get(imagesPath, uniqueFilename);

        // Save the file to the specified directory
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return originalFilename;
    }

    public AccommodationForUserDto getById(UUID accommodationId){
        AccommodationEntity accommodationEntity = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new DataNotFoundException("Accommodation Not Found!"));
        return modelMapper.map(accommodationEntity,AccommodationForUserDto.class);
    }

    public List<AccommodationForUserDto> getByCompany(UUID companyId,Filter filter){
        if(filter == null) filter = new Filter();
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new DataNotFoundException("Company Not Found!"));
        List<AccommodationEntity> accommodationEntities = accommodationRepository.findByCompany(company, filter);
        List<AccommodationForUserDto> accommodation = new ArrayList<>();
        accommodationEntities.forEach((accommodationEntity -> accommodation.add(modelMapper.map(accommodationEntity,AccommodationForUserDto.class))));
        return accommodation;
    }

    public List<AccommodationForUserDto> getAll(Filter filter){
        if(filter == null) filter = new Filter();
        List<AccommodationEntity> all = accommodationRepository.getAll(filter);
        List<AccommodationForUserDto> forUserDto = new ArrayList<>();
        all.forEach((accommodationEntity -> forUserDto.add(modelMapper.map(accommodationEntity,AccommodationForUserDto.class))));
        return forUserDto;
    }

    public AccommodationForUserDto updateName(String newName, UUID accommodationId){
        AccommodationEntity accommodationEntity = accommodationRepository.updateName(newName, accommodationId).orElseThrow(() -> new DataNotFoundException("Accommodation not found!"));
        return modelMapper.map(accommodationEntity,AccommodationForUserDto.class);
    }

    public AccommodationForUserDto updateCompany(UUID accommodationId,UUID companyId){
        AccommodationEntity accommodationEntity = accommodationRepository.updateCompany(accommodationId, companyId).orElseThrow(() -> new DataNotFoundException("Company not found!"));
        return modelMapper.map(accommodationEntity,AccommodationForUserDto.class);
    }

    public void delete(UUID accommodationId){
        AccommodationEntity accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new DataNotFoundException("Accommodation Not Found!"));
        accommodationRepository.delete(accommodation);
    }
}
