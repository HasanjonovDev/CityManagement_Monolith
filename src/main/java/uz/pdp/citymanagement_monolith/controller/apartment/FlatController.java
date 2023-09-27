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
public class FlatController {
    private final FlatService flatService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_FLAT_CRUD','PERMISSION_ALL_CRUD')")
    @PutMapping ("/update/owner/{userId}/{flatId}")
    public ResponseEntity<FlatForUserDto> setOwner(
            @PathVariable UUID userId,
            @PathVariable UUID flatId
    ){
        return ResponseEntity.ok(flatService.setOwner(userId,flatId));
    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','PERMISSION_FLAT_CRUD','PERMISSION_ALL_CRUD')")
    @PutMapping("/remove/owner/{flatId}")
    public ResponseEntity<FlatForUserDto> removeOwner(
            @PathVariable UUID flatId
    ){
        return ResponseEntity.ok(flatService.removeOwner(flatId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/accommodation/")
    public ResponseEntity<List<FlatForUserDto>> getByAccommodationId(
            @PathVariable UUID id,
            @RequestBody(required = false) Filter filter
    ){
        return ResponseEntity.ok(flatService.getAll(id,filter));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/{id}")
    public ResponseEntity<FlatForUserDto> getFlat(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(flatService.getFlatToController(id));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/gt")
    public ResponseEntity<List<FlatForUserDto>> getMine(
            @RequestBody(required = false) Filter filter,
            Principal principal
    ) {
        return ResponseEntity.ok(flatService.getFlat(principal,filter));
    }
}
