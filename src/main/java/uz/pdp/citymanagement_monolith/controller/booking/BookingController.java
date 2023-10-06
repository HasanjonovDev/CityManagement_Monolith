package uz.pdp.citymanagement_monolith.controller.booking;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.service.booking.BookingService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
@PreAuthorize("isAuthenticated()")
public class BookingController {
    private final BookingService bookingService;
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Remove the order"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/removeOrder/{id}")
    public ResponseEntity<ApiResponse> bookFlat(
            @PathVariable UUID id
    ) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK,true,"Successfully deleted"));
    }
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Book the flat"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/book/flat/{flatId}")
    public ResponseEntity<ApiResponse> bookFlat(
            Principal principal,
            @PathVariable UUID flatId
    ) {

        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK,
                true,
                "Successfully booked",
                bookingService.bookSingleFlat(flatId,principal)));
    }
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Confirm one time"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/confirm/{bookingId}")
    public ResponseEntity<ApiResponse> confirm1(
            Principal principal,
            @PathVariable UUID bookingId
    ) {
        bookingService.confirm1(principal,bookingId);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK,
                true,
                "Successfully confirmed"));
    }
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Confirm it fully"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/approve/{bookingId}")
    public ResponseEntity<ApiResponse> approve(
            Principal principal,
            @RequestParam String cardNumber,
            @PathVariable UUID bookingId
    ) {
        bookingService. approve(principal,cardNumber,bookingId);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK,
                true,
                "Successfully booked"));
    }
}
