package uz.pdp.citymanagement_monolith.controller.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.service.booking.BookingService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;

    @DeleteMapping("/removeOrder/{id}")
    public ResponseEntity<ApiResponse> bookFlat(
            @PathVariable UUID id
    ) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK,true,"Successfully deleted"));
    }

    @PutMapping("/book/flat/{flatId}")
    public ResponseEntity<ApiResponse> bookFlat(
            Principal principal,
            @PathVariable UUID flatId) {

        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK,
                true,
                "Successfully booked",
                bookingService.bookSingleFlat(flatId,principal)));
    }
    @PutMapping("/confirm/{bookingId}")
    public ResponseEntity<ApiResponse> confirm1(
            Principal principal,
            @PathVariable UUID bookingId
    ) {
        bookingService.confirm1(principal,bookingId);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK,
                true,
                "Successfully booked"));
    }
    @PutMapping("/approve/{bookingId}")
    public ResponseEntity<ApiResponse> approve(
            Principal principal,
            @RequestParam String cardNumber,
            @PathVariable UUID bookingId
    ) {
        bookingService.approve(principal,cardNumber,bookingId);
        return ResponseEntity.ok(new ApiResponse(
                HttpStatus.OK,
                true,
                "Successfully booked"));
    }
}
