package uz.pdp.citymanagement_monolith.service.apartment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatStatus;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.apartment.AccommodationRepository;
import uz.pdp.citymanagement_monolith.repository.apartment.FlatRepository;
import uz.pdp.citymanagement_monolith.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlatService {
    private final AccommodationRepository accommodationRepository;
    private final FlatRepository flatRepository;
    private final UserService userService;

    public FlatEntity setOwner(Principal principal, UUID flatId){
        FlatEntity flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new DataNotFoundException("Flat Not Found"));
        UserEntity user = userService.getUser(principal.getName());
        flat.setOwnerId(user.getId());
        flat.setStatus(FlatStatus.BUSY);
        return flatRepository.save(flat);
    }

    public FlatEntity removeOwner(UUID flatId){
        FlatEntity flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new DataNotFoundException("Flat Not Found!"));
        flat.setOwnerId(flat.getCompany().getId());
        flat.setStatus(FlatStatus.AVAILABLE);
        return flatRepository.save(flat);
    }

    public List<FlatEntity> getAll(UUID id) {
        return accommodationRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Accommodation Not Found"))
                .getFlats();
    }

    public FlatEntity getFlat(UUID id) {
        return flatRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Flat Not Found!"));
    }
}
