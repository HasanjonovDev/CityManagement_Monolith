package uz.pdp.citymanagement_monolith.controller.apartment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.AccommodationCreateDto;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.AccommodationEntity;
import uz.pdp.citymanagement_monolith.service.apartment.AccommodationService;


import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment/api/v1/accommodation")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @PreAuthorize("hasAnyRole('COMPANY_ADMIN','ADMIN')")
    @PostMapping("/add/premium")
    public ResponseEntity<AccommodationEntity> savePremium(
            @Valid @RequestBody AccommodationCreateDto accommodationCreateDto,
            Principal principal,
            BindingResult bindingResult
    ) {
        return ResponseEntity.ok(accommodationService.savePremiumAccommodation(accommodationCreateDto,principal,bindingResult));
    }

    @PreAuthorize("hasAnyRole('COMPANY_ADMIN','ADMIN')")
    @PostMapping("/add/economy")
    public ResponseEntity<AccommodationEntity> saveEconomy(
            Principal principal,
            @Valid @RequestBody AccommodationCreateDto accommodationCreateDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(accommodationService.saveEconomyAccommodation(accommodationCreateDto,principal, bindingResult));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/byId/{accommodationId}")
    public ResponseEntity<AccommodationEntity> getById(
            @PathVariable UUID accommodationId
    ){
        return ResponseEntity.ok(accommodationService.getById(accommodationId));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/byCompany/{companyId}")
    public ResponseEntity<List<AccommodationEntity>> getByCompany(
            @PathVariable UUID companyId
    ){
        return ResponseEntity.ok(accommodationService.getByCompany(companyId));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/all")
    public ResponseEntity<List<AccommodationEntity>> getAll(){
        return ResponseEntity.ok(accommodationService.getAll());
    }
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN','ADMIN')")
    @PutMapping("/{newName}/{accommodationId}/update")
    public ResponseEntity<AccommodationEntity> updateName(
            @PathVariable String newName,
            @PathVariable UUID accommodationId
            ){
        return ResponseEntity.ok(accommodationService.updateName(newName,accommodationId));
    }
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN','ADMIN')")
    @PutMapping("/{companyId}/{accommodationId}/update")
    public ResponseEntity<AccommodationEntity> updateCompany(
            @PathVariable UUID accommodationId,
            @PathVariable UUID companyId
    ){
        return ResponseEntity.ok(accommodationService.updateCompany(accommodationId,companyId));
    }
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN','ADMIN')")
    @DeleteMapping("/{accommodationId}/delete")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable UUID accommodationId
    ){
        accommodationService.delete(accommodationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
