package uz.pdp.citymanagement_monolith.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.citymanagement_monolith.domain.dto.report.WeekReport;
import uz.pdp.citymanagement_monolith.service.report.ReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
@PreAuthorize("hasAuthority('PERMISSION_SEE_REPORTS')")
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/weekly")
    public ResponseEntity<WeekReport> weekReport() {
        return ResponseEntity.ok(reportService.reportPerWeek());
    }
}