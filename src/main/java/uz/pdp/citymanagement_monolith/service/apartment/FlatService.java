package uz.pdp.citymanagement_monolith.service.apartment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.FlatForUserDto;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatStatus;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.apartment.FlatRepositoryImpl;
import uz.pdp.citymanagement_monolith.service.user.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlatService {
    private final FlatRepositoryImpl flatRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public FlatForUserDto setOwner(Principal principal, UUID flatId){
        FlatEntity flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new DataNotFoundException("Flat Not Found"));
        UserEntity user = userService.getUser(principal.getName());
        flat.setOwner(user);
        flat.setStatus(FlatStatus.BUSY);
        FlatEntity save = flatRepository.save(flat);
        return modelMapper.map(save, FlatForUserDto.class);
    }

    public FlatForUserDto removeOwner(UUID flatId){
        FlatEntity flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new DataNotFoundException("Flat Not Found!"));
        flat.setOwner(flat.getAccommodation().getCompany().getOwner());
        flat.setStatus(FlatStatus.AVAILABLE);
        FlatEntity save = flatRepository.save(flat);
        return modelMapper.map(save, FlatForUserDto.class);
    }

    public List<FlatForUserDto> getAll(UUID id, Filter filter) {
        List<FlatEntity> flats = flatRepository.findByAccommodation(id,filter);
        List<FlatForUserDto> flatsForUser = new ArrayList<>();
        flats.forEach((flat) -> flatsForUser.add(modelMapper.map(flat, FlatForUserDto.class)));
        return flatsForUser;
    }

    public FlatForUserDto getFlatToController(UUID id) {
        FlatEntity flatEntity = flatRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Flat Not Found!"));
        return modelMapper.map(flatEntity, FlatForUserDto.class);
    }
    public FlatEntity getFlat(UUID id) {
        return flatRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Flat Not Found!"));
    }
}
