package uz.pdp.citymanagement_monolith.service.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingStatus;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingType;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.exception.NotAcceptableException;
import uz.pdp.citymanagement_monolith.repository.booking.BookingRepository;
import uz.pdp.citymanagement_monolith.service.MailService;
import uz.pdp.citymanagement_monolith.service.UserService;
import uz.pdp.citymanagement_monolith.service.apartment.FlatService;
import uz.pdp.citymanagement_monolith.service.payment.PaymentService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlatService flatService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final MailService mailService;
    public void cancelBooking(UUID orderId){
        BookingEntity booking =
                bookingRepository.findById(orderId).orElseThrow(() -> new DataNotFoundException("There is no such orders"));
        booking.setStatus(BookingStatus.CLOSED);
    }
    @Scheduled(cron = "0 0 * * *")
    private void deleteClosedBookings() {
        List<BookingEntity> bookingEntities = bookingRepository.findAllByCreatedTimeBefore(
                new Date(System.currentTimeMillis() - 86400000)
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        bookingEntities.forEach((bookingEntity -> {if(Objects.equals(bookingEntity.getStatus(), BookingStatus.CLOSED)) bookingRepository.delete(bookingEntity);}));
    }

    public BookingEntity bookSingleFlat(UUID flatId, Principal principal) {
        UUID userId = userService.getUser(principal.getName()).getId();
        UserEntity user = userService.getUserById(userId);
        FlatEntity flat = flatService.getFlat(flatId);
        BookingEntity build = BookingEntity.builder()
                .fromWhomId(flat.getOwnerId())
                .bookingNumber((long) (getMax() + 1))
                .ownerId(userId)
                .type(BookingType.FLAT)
                .orderId(flatId)
                .endTime(LocalDateTime.now().plusMonths(1))
                .totalPrice(flat.getPricePerMonth())
                .status(BookingStatus.CREATED)
                .build();
        mailService.send1ApprovedMessage(user.getEmail(),flat.getNumber());
        return bookingRepository.save(build);
    }
    private int getMax() {
        try {
            return bookingRepository.getMax();
        }catch (Exception e) {
            return 0;
        }
    }

    public void confirm1(Principal principal, UUID bookingId) {
        UserEntity user = userService.getUser(principal.getName());
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() -> new DataNotFoundException("Booking not found!"));
        if (!Objects.equals(user.getId(), bookingEntity.getFromWhomId())) throw new NotAcceptableException("It is not your booking!");
        bookingEntity.setStatus(BookingStatus.IN_PROGRESS);
        bookingRepository.save(bookingEntity);
        FlatEntity flat = flatService.getFlat(bookingEntity.getOrderId());
        mailService.send2ApprovedMessage(principal.getName(),flat.getNumber());
    }

    public void approve(Principal principal,String senderCardNumber, UUID bookingId) {
        UserEntity customer = userService.getUser(principal.getName());
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() -> new DataNotFoundException("Booking not found!"));
        if (!Objects.equals(customer.getId(), bookingEntity.getFromWhomId())) throw new NotAcceptableException("It is not your booking!");
        bookingEntity.setStatus(BookingStatus.FULLY_APPROVED);
        bookingRepository.save(bookingEntity);
        FlatEntity flat = flatService.getFlat(bookingEntity.getOrderId());
        UserEntity renter = userService.getUserById(bookingEntity.getFromWhomId());
        mailService.sendFullApproveMessageToCustomer(principal.getName(),flat);
        mailService.sendFullApprovalToRenter(renter.getEmail(),customer.getEmail(), flat.getNumber());
        paymentService.pay(senderCardNumber,flat.getId(), flat.getPricePerMonth());
        flatService.setOwner(principal,flat.getId());
    }
}


