package uz.pdp.citymanagement_monolith.controller.apartment;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.CompanyCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.apartment.CompanyForUserDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.apartment.CompanyService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apartment/api/v1/company")
public class CompanyController {

    private final CompanyService companyService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_COMPANY_CRUD','PERMISSION_ALL_CRUD')")
    @PostMapping("/add")
    public ResponseEntity<CompanyForUserDto> add(
            Principal principal,
            @Valid @RequestBody CompanyCreateDto companyCreateDto,
            BindingResult bindingResult
    ){
        return ResponseEntity.ok(companyService.save(principal,companyCreateDto,bindingResult));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/{id}")
    public ResponseEntity<CompanyForUserDto> get(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(companyService.get(id));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}/get")
    public ResponseEntity<List<CompanyForUserDto>> getList(
            @PathVariable UUID userId,
            @RequestBody Filter filter
    ) {
        return ResponseEntity.ok(companyService.getList(userId,filter));
    }
}
