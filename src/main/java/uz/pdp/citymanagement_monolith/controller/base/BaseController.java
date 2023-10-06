package uz.pdp.citymanagement_monolith.controller.base;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.citymanagement_monolith.service.apartment.AccommodationService;

@RestController
@RequestMapping("/base")
@RequiredArgsConstructor
public class BaseController {
    private final AccommodationService accommodationService;
    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get the licence of the project"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<Resource> getLicence() {
        return accommodationService.getLicence();
    }
}
