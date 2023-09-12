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

    @PreAuthorize("hasAnyRole('COMPANY_ADMIN','SUPER_ADMIN')")
    @PostMapping("/add/premium")
    public ResponseEntity<AccommodationEntity> savePremium(
            @Valid @RequestBody AccommodationCreateDto accommodationCreateDto,
            Principal principal,
            BindingResult bindingResult
    ) {
        return ResponseEntity.ok(accommodationService.savePremiumAccommodation(accommodationCreateDto,principal,bindingResult));
    }

    @PreAuthorize("hasAnyRole('COMPANY_ADMIN','ROLE_SUPER_ADMIN')")
    @PostMapping("/add/economy")
    public ResponseEntity<AccommodationEntity> saveEconomy(
            Principal principal,
            @Valid @RequestBody AccommodationCreateDto accommodationCreateDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(accommodationService.saveEconomyAccommodation(accommodationCreateDto,principal, bindingResult));
    }

    @GetMapping("/get/byId")
    public ResponseEntity<AccommodationEntity> getById(
            @RequestParam UUID accommodationId
    ){
        return ResponseEntity.ok(accommodationService.getById(accommodationId));
    }

    @GetMapping("/get/byCompany")
    public ResponseEntity<List<AccommodationEntity>> getByCompany(
            @RequestParam UUID companyId
    ){
        return ResponseEntity.ok(accommodationService.getByCompany(companyId));
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<AccommodationEntity>> getAll(){
        return ResponseEntity.ok(accommodationService.getAll());
    }

    @PutMapping("/update/name")
    public ResponseEntity<AccommodationEntity> updateName(
            @RequestParam String newName,
            @RequestParam UUID accommodationId
            ){
        return ResponseEntity.ok(accommodationService.updateName(newName,accommodationId));
    }

    @PutMapping("/update/company")
    public ResponseEntity<AccommodationEntity> updateCompany(
            @RequestParam UUID accommodationId,
            @RequestParam String companyName
    ){
        return ResponseEntity.ok(accommodationService.updateCompany(accommodationId,companyName));
    }

    @DeleteMapping("/delete")
    public HttpStatus delete(
            @RequestParam UUID accommodationId
    ){
        accommodationService.delete(accommodationId);
        return HttpStatus.OK;
    }
}
