package uz.pdp.citymanagement_monolith.service.apartment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.AccommodationCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.AccommodationForUserDto;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.*;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.exception.NotAcceptableException;
import uz.pdp.citymanagement_monolith.exception.RequestValidationException;
import uz.pdp.citymanagement_monolith.repository.apartment.AccommodationRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.apartment.CompanyRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.apartment.FlatRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserRepositoryImpl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public AccommodationForUserDto savePremiumAccommodation(AccommodationCreateDto accommodationCreateDto, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            throw new RequestValidationException(allErrors);
        }

        UserEntity user = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));

        CompanyEntity companyEntity = companyRepository.findById(accommodationCreateDto.getCompanyId())
                .orElseThrow(() -> new DataNotFoundException("Company Not Found!"));
        if (!Objects.equals(companyEntity.getOwner().getId(), user.getId()))
            throw new NotAcceptableException("It is not your company!");
        AccommodationEntity accommodation = modelMapper.map(accommodationCreateDto, AccommodationEntity.class);
        accommodation.setFloors(4);
        accommodation.setCompany(companyEntity);
        accommodation.setNumberOfFlats(8);
        accommodation.setLocationEntity(accommodationCreateDto.getLocationEntity());
        accommodation.setName(accommodationCreateDto.getName());
        accommodation.setNumber(accommodationRepository.getMax() + 1);
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
                        .fullPrice(100000.0)
                        .whichFloor(floor)
                        .flatType(FlatType.PREMIUM)
                        .owner(companyEntity.getOwner())
                        .status(FlatStatus.AVAILABLE)
                        .accommodation(savedAccommodation)
                        .build();
                flatRepository.save(premiumFlat);
            }
            floor++;
        }
        return modelMapper.map(savedAccommodation, AccommodationForUserDto.class);

    }

    public AccommodationForUserDto saveEconomyAccommodation(AccommodationCreateDto accommodationCreateDto, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationException(bindingResult.getAllErrors());
        }

        UserEntity user = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        CompanyEntity companyEntity = companyRepository.findById(accommodationCreateDto.getCompanyId())
                .orElseThrow(() -> new DataNotFoundException("Company Not Found!"));
        if (!Objects.equals(companyEntity.getOwner().getId(), user.getId()))
            throw new NotAcceptableException("It is not your company!");
        AccommodationEntity accommodation = modelMapper.map(accommodationCreateDto, AccommodationEntity.class);
        accommodation.setFloors(9);
        accommodation.setCompany(companyEntity);
        accommodation.setNumberOfFlats(36);
        accommodation.setLocationEntity(accommodationCreateDto.getLocationEntity());
        accommodation.setName(accommodationCreateDto.getName());
        accommodation.setNumber(accommodationRepository.getMax() + 1);
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
                        .fullPrice(60000.0)
                        .whichFloor(floor)
                        .flatType(FlatType.ECONOMY)
                        .owner(companyEntity.getOwner())
                        .accommodation(savedAccommodation)
                        .status(FlatStatus.AVAILABLE)
                        .build();
                flatRepository.save(premiumFlat);
            }
            floor++;
        }
        return modelMapper.map(savedAccommodation, AccommodationForUserDto.class);
    }

    public AccommodationForUserDto getById(UUID accommodationId) {
        AccommodationEntity accommodationEntity = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new DataNotFoundException("Accommodation Not Found!"));
        return modelMapper.map(accommodationEntity, AccommodationForUserDto.class);
    }

    public List<AccommodationForUserDto> getByCompany(UUID companyId, Filter filter) {
        if (filter == null) filter = new Filter();
        CompanyEntity company = companyRepository.findById(companyId)
                .orElseThrow(() -> new DataNotFoundException("Company Not Found!"));
        List<AccommodationEntity> accommodationEntities = accommodationRepository.findByCompany(company, filter);
        List<AccommodationForUserDto> accommodation = new ArrayList<>();
        accommodationEntities.forEach((accommodationEntity -> accommodation.add(modelMapper.map(accommodationEntity, AccommodationForUserDto.class))));
        return accommodation;
    }

    public List<AccommodationForUserDto> getAll(Filter filter) {
        if (filter == null) filter = new Filter();
        List<AccommodationEntity> all = accommodationRepository.getAll(filter);
        List<AccommodationForUserDto> forUserDto = new ArrayList<>();
        all.forEach((accommodationEntity -> forUserDto.add(modelMapper.map(accommodationEntity, AccommodationForUserDto.class))));
        return forUserDto;
    }

    public AccommodationForUserDto updateName(String newName, UUID accommodationId) {
        AccommodationEntity accommodationEntity = accommodationRepository.updateName(newName, accommodationId).orElseThrow(() -> new DataNotFoundException("Accommodation not found!"));
        return modelMapper.map(accommodationEntity, AccommodationForUserDto.class);
    }

    public AccommodationForUserDto updateCompany(UUID accommodationId, UUID companyId) {
        AccommodationEntity accommodationEntity = accommodationRepository.updateCompany(accommodationId, companyId).orElseThrow(() -> new DataNotFoundException("Company not found!"));
        return modelMapper.map(accommodationEntity, AccommodationForUserDto.class);
    }

    public void delete(UUID accommodationId) {
        AccommodationEntity accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new DataNotFoundException("Accommodation Not Found!"));
        accommodationRepository.delete(accommodation);
    }

    public ResponseEntity<Resource> getLicence() {
        Path path = Paths.get("0070.pdf");
        Resource resource = new FileSystemResource(path.toFile());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=licence.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(path.toFile().length())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    public List<AccommodationForUserDto> getAllByOwner(UUID userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found!"));
        List<AccommodationEntity> accommodations = accommodationRepository.findByCompanyOwner(userEntity);
        List<AccommodationForUserDto> forUserDto = new ArrayList<>();
        accommodations.forEach((accommodation) -> forUserDto.add(modelMapper.map(accommodation, AccommodationForUserDto.class)));
        return forUserDto;
    }
}
