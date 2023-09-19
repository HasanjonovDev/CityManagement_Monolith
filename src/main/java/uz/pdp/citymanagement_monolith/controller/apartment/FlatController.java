package uz.pdp.citymanagement_monolith.controller.apartment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.FlatForUserDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.apartment.FlatService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment/api/v1/flat")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_FLAT_CRUD','PERMISSION_ALL_CRUD')")
public class FlatController {
    private final FlatService flatService;


    @PutMapping ("/update/setOwner")
    public ResponseEntity<FlatForUserDto> setOwner(
            Principal principal,
            @RequestParam UUID flatId
    ){
        return ResponseEntity.ok(flatService.setOwner(principal,flatId));
    }

    @PutMapping("/update/removeOwner")
    public ResponseEntity<FlatForUserDto> removeOwner(
            @RequestParam UUID flatId
    ){
        return ResponseEntity.ok(flatService.removeOwner(flatId));
    }


    @GetMapping("/get/accommodation/{id}")
    public ResponseEntity<List<FlatForUserDto>> getByAccommodationId(
            @PathVariable UUID id,
            @RequestBody Filter filter
    ){
        return ResponseEntity.ok(flatService.getAll(id,filter));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<FlatForUserDto> getFlat(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(flatService.getFlatToController(id));
    }
}
