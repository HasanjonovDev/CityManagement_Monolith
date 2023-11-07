package uz.pdp.citymanagement_monolith.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.report.ReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
public class ReportController {
    private final ReportService reportService;

    @ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Weekly report"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @GetMapping("/weekly")
    @PreAuthorize("hasAnyAuthority('PERMISSION_SEE_REPORTS','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN')")
    public ResponseEntity<uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse> weekReport(
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse.builder()
                .message("OK")
                .status(HttpStatus.OK)
                .success(true)
                .data(reportService.reportPerWeek(filter))
                .build());
    }
}