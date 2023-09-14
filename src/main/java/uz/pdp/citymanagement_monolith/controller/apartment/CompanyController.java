package uz.pdp.citymanagement_monolith.controller.apartment;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.CompanyCreateDto;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.citymanagement_monolith.service.apartment.CompanyService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment/api/v1/company")
@PreAuthorize("hasAnyAuthority('ADMIN','PERMISSION_COMPANY_CRUD','PERMISSION_ALL_CRUD')")
public class CompanyController {

    private final CompanyService companyService;


    @PostMapping("/add")
    public ResponseEntity<CompanyEntity> add(
            Principal principal,
            @Valid @RequestBody CompanyCreateDto companyCreateDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(companyService.save(principal,companyCreateDto,bindingResult));
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<CompanyEntity> get(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(companyService.get(id));
    }
    @GetMapping("/{userId}/get")
    public ResponseEntity<List<CompanyEntity>> getList(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(companyService.getList(userId));
    }
}
