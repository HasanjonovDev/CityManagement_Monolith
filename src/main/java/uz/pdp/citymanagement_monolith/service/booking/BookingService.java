package uz.pdp.citymanagement_monolith.service.booking;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.booking.BookingForUserDto;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.AccommodationEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.*;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.domain.entity.system.SystemBalanceEntity;
import uz.pdp.citymanagement_monolith.domain.entity.system.SystemBalanceStatus;
import uz.pdp.citymanagement_monolith.domain.entity.user.MessageState;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserInboxEntity;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.exception.NotAcceptableException;
import uz.pdp.citymanagement_monolith.repository.apartment.FlatRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.booking.BookingRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.booking.BuyHistoryRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.payment.CardRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.system.SystemBalanceRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserInboxRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserRepositoryImpl;
import uz.pdp.citymanagement_monolith.service.user.MailService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepositoryImpl bookingRepository;
    private final FlatRepositoryImpl flatRepository;
    private final UserRepositoryImpl userRepository;
    private final CardRepositoryImpl paymentRepository;
    private final UserInboxRepositoryImpl userInboxRepository;
    private final SystemBalanceRepositoryImpl systemBalanceRepository;
    private final BuyHistoryRepositoryImpl buyHistoryRepository;
    private final MailService mailService;
    private final ModelMapper modelMapper;
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

    public BookingForUserDto bookSingleFlat(String cardNumber,UUID flatId, Principal principal) {
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        FlatEntity flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new DataNotFoundException("Flat not found!"));
        BookingEntity booking = BookingEntity.builder()
                .fromWhom(flat.getOwner())
                .bookingNumber((long) (getMax() + 1))
                .owner(user)
                .type(BookingType.FLAT)
                .orderId(flatId)
                .endTime(LocalDateTime.now().plusMonths(1))
                .totalPrice(flat.getPricePerMonth())
                .status(BookingStatus.CREATED)
                .build();
        UserInboxEntity userInboxEntity = UserInboxEntity.builder()
                .message("Hey there is a book request to your flat №" + flat.getNumber())
                .fromWhom(user)
                .toWhom(flat.getOwner())
                .type("BOOKING")
                .state(MessageState.PENDING)
                .build();
        BookingEntity saved = bookingRepository.save(booking);
        SystemBalanceEntity systemBalance = SystemBalanceEntity.builder()
                .senderCard(paymentRepository.findCardEntityByNumber(cardNumber)
                        .orElseThrow(() -> new DataNotFoundException("Card not found!")))
                .receiverCard(flat.getAccommodation().getCompany().getCard())
                .user(user)
                .amount(booking.getTotalPrice())
                .status(SystemBalanceStatus.ACTIVE)
                .forWhat(saved.getId())
                .build();
        systemBalanceRepository.save(systemBalance);
        userInboxRepository.save(userInboxEntity);
        mailService.send1ApprovedMessage(user.getEmail(),flat.getNumber());
        return modelMapper.map(saved,BookingForUserDto.class);
    }
    private int getMax() {
        try {
            return bookingRepository.getMax();
        }catch (Exception e) {
            return 0;
        }
    }
    @Deprecated
    public void confirm1(Principal principal, UUID bookingId) {
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() -> new DataNotFoundException("Booking not found!"));
        if (!Objects.equals(user.getId(), bookingEntity.getFromWhom().getId())) throw new NotAcceptableException("It is not your booking!");
        bookingEntity.setStatus(BookingStatus.IN_PROGRESS);
        bookingRepository.save(bookingEntity);
        FlatEntity flat = flatRepository.findById(bookingEntity.getOrderId())
                        .orElseThrow(() -> new DataNotFoundException("Flat not found!"));
        mailService.send2ApprovedMessage(principal.getName(),flat.getNumber());
    }
    @Deprecated
    public void approve(Principal principal,String senderCardNumber, UUID bookingId) {
        UserEntity customer = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        BookingEntity bookingEntity = bookingRepository.findById(bookingId).orElseThrow(() -> new DataNotFoundException("Booking not found!"));
        if (!Objects.equals(customer.getId(), bookingEntity.getFromWhom().getId())) throw new NotAcceptableException("It is not your booking!");
        bookingEntity.setStatus(BookingStatus.FULLY_APPROVED);
        bookingRepository.save(bookingEntity);
        FlatEntity flat = flatRepository.findById(bookingEntity.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Flat not found!"));
        AccommodationEntity accommodation = flat.getAccommodation();
        UserEntity renter = bookingEntity.getFromWhom();
        mailService.sendFullApproveMessageToCustomer(principal.getName(),flat);
        mailService.sendFullApprovalToRenter(renter.getEmail(),customer.getEmail(), flat.getNumber());
        paymentRepository.pay(senderCardNumber,accommodation.getCompany().getCard(), flat.getPricePerMonth());
        flatRepository.updateOwner(principal,flat.getId());
    }

    public void buyFlat(Principal principal, String cardNumber, UUID flatId) {
        FlatEntity flatEntity = flatRepository.findById(flatId)
                .orElseThrow(() -> new DataNotFoundException("Flat not found!"));
        UserEntity userEntity = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        CardEntity cardEntity = paymentRepository.findCardEntityByNumber(cardNumber)
                .orElseThrow(() -> new DataNotFoundException("Card not found!"));
        if(Objects.equals(flatEntity.getOwner(),userEntity)) throw new NotAcceptableException("It is your flat!");
        if(!Objects.equals(cardEntity.getOwner().getId(),userEntity.getId())) throw new NotAcceptableException("It is not your card!");
        BuyHistoryEntity buyHistoryEntity = BuyHistoryEntity.builder()
                .buyer(userEntity)
                .seller(flatEntity.getOwner())
                .good(flatId)
                .amount(flatEntity.getFullPrice())
                .status(BuyHistoryStatus.NOT_PAID)
                .build();
        SystemBalanceEntity systemBalance = SystemBalanceEntity.builder()
                .forWhat(flatId)
                .status(SystemBalanceStatus.ACTIVE)
                .senderCard(cardEntity)
                .amount(flatEntity.getFullPrice())
                .receiverCard(null)
                .user(userEntity)
                .build();
        UserInboxEntity userInboxEntity = UserInboxEntity.builder()
                .state(MessageState.PENDING)
                .type("PURCHASE")
                .message("Hey someone want to buy your flat №" + flatEntity.getNumber())
                .fromWhom(userEntity)
                .toWhom(flatEntity.getOwner())
                .build();
        systemBalanceRepository.save(systemBalance);
        buyHistoryRepository.save(buyHistoryEntity);
        userInboxRepository.save(userInboxEntity);
    }
}


