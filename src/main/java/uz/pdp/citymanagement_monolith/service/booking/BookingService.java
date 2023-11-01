package uz.pdp.citymanagement_monolith.service.booking;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.booking.BookingForUserDto;
import uz.pdp.citymanagement_monolith.domain.dto.booking.PreOrderRequestDto;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatStatus;
import uz.pdp.citymanagement_monolith.domain.entity.booking.*;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.domain.entity.system.SystemBalanceEntity;
import uz.pdp.citymanagement_monolith.domain.entity.system.SystemBalanceStatus;
import uz.pdp.citymanagement_monolith.domain.entity.user.MessageState;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserInboxEntity;
import uz.pdp.citymanagement_monolith.exception.BadRequestException;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.exception.NotAcceptableException;
import uz.pdp.citymanagement_monolith.repository.apartment.FlatRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.booking.BookingDaysStatusRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.booking.BookingRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.booking.BuyHistoryRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.booking.PreOrderBookingRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.payment.CardRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.system.SystemBalanceRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserInboxRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserRepositoryImpl;
import uz.pdp.citymanagement_monolith.service.user.MailService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final PreOrderBookingRepositoryImpl preOrderBookingRepository;
    private final BookingDaysStatusRepositoryImpl bookingDaysStatusRepository;
    private final BookingRepositoryImpl bookingRepository;
    private final FlatRepositoryImpl flatRepository;
    private final UserRepositoryImpl userRepository;
    private final CardRepositoryImpl paymentRepository;
    private final UserInboxRepositoryImpl userInboxRepository;
    private final SystemBalanceRepositoryImpl systemBalanceRepository;
    private final BuyHistoryRepositoryImpl buyHistoryRepository;
    private final MailService mailService;
    private final ModelMapper modelMapper;

    public ApiResponse preOrder(PreOrderRequestDto preOrderRequestDto, Principal principal) {
        Optional<UserEntity> userEntityByEmail = userRepository.findUserEntityByEmail(principal.getName());
        UserEntity userEntity;
        if (userEntityByEmail.isEmpty())
            return ApiResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .success(false)
                    .message("User not found!")
                    .build();
        else userEntity = userEntityByEmail.get();
        FlatEntity preOrderFlat = flatRepository.findById(preOrderRequestDto.getFlatId())
                .orElseThrow(() -> new BadRequestException("Flat not found!"));
        PreOrderBookingEntity preOrderBookingEntity = modelMapper.map(preOrderRequestDto, PreOrderBookingEntity.class);
        preOrderBookingEntity.setOwner(userEntity);
        preOrderBookingEntity.setPrePayAmount(preOrderFlat.getPricePerMonth() / 30 * preOrderBookingEntity.getDays());
        BookingDaysStatusEntity bookingDaysStatusEntity = BookingDaysStatusEntity.builder()
                .flat(preOrderFlat)
                .status(FlatStatus.BUSY)
                .date(preOrderRequestDto.getDate())
                .build();
        PreOrderBookingEntity savedPreOrder = preOrderBookingRepository.save(preOrderBookingEntity);
        bookingDaysStatusRepository.save(bookingDaysStatusEntity);
        return ApiResponse.builder()
                .status(HttpStatus.OK)
                .success(true)
                .message("OK")
                .data(savedPreOrder)
                .build();
    }

    public void cancelBooking(UUID orderId) {
        BookingEntity booking =
                bookingRepository.findById(orderId).orElseThrow(() -> new DataNotFoundException("There is no such orders"));
        booking.setStatus(BookingStatus.CLOSED);
    }

    @Scheduled(cron = "0 0 * * *")
    private void deleteClosedBookings() {
        List<BookingEntity> bookingEntities = bookingRepository.findAllByCreatedTimeBefore(
                new Date(System.currentTimeMillis() - 86400000)
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        bookingEntities.forEach((bookingEntity -> {
            if (Objects.equals(bookingEntity.getStatus(), BookingStatus.CLOSED))
                bookingRepository.delete(bookingEntity);
        }));
    }

    public ApiResponse bookSingleFlat(String cardNumber, UUID flatId, Principal principal) {
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        FlatEntity flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new DataNotFoundException("Flat not found!"));
        Optional<BookingDaysStatusEntity> status = bookingDaysStatusRepository.findBookingDaysStatusEntityByFlat(flat);
        if (status.isEmpty()) {
            Optional<PreOrderBookingEntity> preOrderBookingEntityByFlat =
                    preOrderBookingRepository.findPreOrderBookingEntityByFlat(flat);
            if(preOrderBookingEntityByFlat.isPresent()) {
                PreOrderBookingEntity preOrderBookingEntity = preOrderBookingEntityByFlat.get();
                if(flat.getStatus().equals(FlatStatus.BUSY)
                        || preOrderBookingEntity.getDate().equals(new Date())) {
                    return ApiResponse.builder()
                            .message("This flat is already booked!")
                            .success(false)
                            .status(HttpStatus.BAD_REQUEST)
                            .build();
                }
            }
        }

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
        mailService.send1ApprovedMessage(user.getEmail(), flat.getNumber());
        return ApiResponse.builder()
                .message("Successfully booked!")
                .status(HttpStatus.OK)
                .success(true)
                .data(modelMapper.map(saved, BookingForUserDto.class))
                .build();
    }

    private int getMax() {
        try {
            return bookingRepository.getMax();
        } catch (Exception e) {
            return 0;
        }
    }

    public void buyFlat(Principal principal, String cardNumber, UUID flatId) {
        FlatEntity flatEntity = flatRepository.findById(flatId)
                .orElseThrow(() -> new DataNotFoundException("Flat not found!"));
        UserEntity userEntity = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        CardEntity cardEntity = paymentRepository.findCardEntityByNumber(cardNumber)
                .orElseThrow(() -> new DataNotFoundException("Card not found!"));
        if (Objects.equals(flatEntity.getOwner(), userEntity)) throw new NotAcceptableException("It is your flat!");
        if (!Objects.equals(cardEntity.getOwner().getId(), userEntity.getId()))
            throw new NotAcceptableException("It is not your card!");
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


