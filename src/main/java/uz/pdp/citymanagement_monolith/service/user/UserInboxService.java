package uz.pdp.citymanagement_monolith.service.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserInboxCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserInboxForUserDto;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatStatus;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingStatus;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BuyHistoryEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BuyHistoryStatus;
import uz.pdp.citymanagement_monolith.domain.entity.system.SystemBalanceEntity;
import uz.pdp.citymanagement_monolith.domain.entity.system.SystemBalanceStatus;
import uz.pdp.citymanagement_monolith.domain.entity.user.MessageState;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserInboxEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.exception.NotAcceptableException;
import uz.pdp.citymanagement_monolith.repository.apartment.FlatRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.booking.BookingRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.booking.BuyHistoryRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.payment.CardRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.system.SystemBalanceRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserInboxRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserRepositoryImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInboxService {
    private final UserInboxRepositoryImpl userInboxRepository;
    private final UserRepositoryImpl userRepository;
    private final SystemBalanceRepositoryImpl systemBalanceRepository;
    private final BuyHistoryRepositoryImpl buyHistoryRepository;
    private final BookingRepositoryImpl bookingRepository;
    private final CardRepositoryImpl cardRepository;
    private final FlatRepositoryImpl flatRepository;
    private final ModelMapper modelMapper;

    public UserInboxForUserDto addNewMessage(UUID userId, UserInboxCreateDto userInboxCreateDto, Principal principal) {
        UserInboxEntity userInboxEntity = modelMapper.map(userInboxCreateDto, UserInboxEntity.class);
        userInboxEntity.setToWhom(userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found!")));
        userInboxEntity.setFromWhom(userRepository.findUserEntityByEmail(principal.getName()).orElseThrow(() -> new DataNotFoundException("User not found!")));
        UserInboxEntity saved = userInboxRepository.save(userInboxEntity);
        return modelMapper.map(saved, UserInboxForUserDto.class);
    }

    public List<UserInboxForUserDto> getAllUserInbox(UUID userId, Filter filter) {
        if (filter == null) filter = new Filter();
        List<UserInboxEntity> allByOwner = userInboxRepository.getAllByToWhom(userId, filter);
        List<UserInboxForUserDto> forUserDto = new ArrayList<>();
        allByOwner.forEach((inbox) -> forUserDto.add(modelMapper.map(inbox, UserInboxForUserDto.class)));
        return forUserDto;
    }

    public UserInboxForUserDto getInbox(UUID inboxId) {
        UserInboxEntity userInboxEntity = userInboxRepository.findById(inboxId)
                .orElseThrow(() -> new DataNotFoundException("Inbox not found!"));
        return modelMapper.map(userInboxEntity, UserInboxForUserDto.class);
    }

    public ApiResponse approveBooking(UUID inboxId) {
        UserInboxEntity userInboxEntity = userInboxRepository.findById(inboxId)
                .orElseThrow(() -> new DataNotFoundException("Inbox not found!"));
        userInboxEntity.setState(MessageState.APPROVED);
        UserEntity user = userInboxEntity.getFromWhom();
        SystemBalanceEntity systemBalanceEntity = systemBalanceRepository.findByUser(user)
                .orElseThrow(() -> new DataNotFoundException("System balance not found!"));
        BookingEntity bookingEntity = bookingRepository.findById(systemBalanceEntity.getForWhat())
                .orElseThrow(() -> new DataNotFoundException("Booking not found!"));
        bookingEntity.setStatus(BookingStatus.FULLY_APPROVED);
        FlatEntity flatEntity = flatRepository.findById(bookingEntity.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Flat not found!"));
        flatEntity.setOwner(user);
        flatEntity.setStatus(FlatStatus.BUSY);
        Long pay = cardRepository.pay(systemBalanceEntity.getSenderCard().getNumber(), systemBalanceEntity.getReceiverCard(), systemBalanceEntity.getAmount());
        if (pay == 1) throw new NotAcceptableException("Not enough credentials");
        if (pay == 0) {
            systemBalanceEntity.setStatus(SystemBalanceStatus.EXPIRED);
            systemBalanceRepository.save(systemBalanceEntity);
            userInboxRepository.save(userInboxEntity);
            bookingRepository.save(bookingEntity);
            flatRepository.save(flatEntity);
            return ApiResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .message("Successfully approve")
                    .build();
        }
        return ApiResponse.builder()
                .success(false)
                .status(HttpStatus.NOT_ACCEPTABLE)
                .message("Error occurred")
                .build();
    }
    public ApiResponse approveBuy(UUID inboxId, String cardNumber) {
        UserInboxEntity userInboxEntity = userInboxRepository.findById(inboxId)
                .orElseThrow(() -> new DataNotFoundException("Inbox not found!"));
        userInboxEntity.setState(MessageState.APPROVED);
        UserEntity user = userInboxEntity.getFromWhom();
        SystemBalanceEntity systemBalanceEntity = systemBalanceRepository.findByUser(user)
                .orElseThrow(() -> new DataNotFoundException("System balance not found!"));
        BuyHistoryEntity buyHistoryEntity = buyHistoryRepository.findByBuyer(user)
                .orElseThrow(() -> new DataNotFoundException("Buy history not found!"));
        Long pay = cardRepository.pay(systemBalanceEntity.getSenderCard().getNumber(),
                cardRepository.findCardEntityByNumber(cardNumber).orElseThrow(() -> new DataNotFoundException("Card not found!")),
                systemBalanceEntity.getAmount());
        if (pay == 1) throw new NotAcceptableException("Low credits!");
        if(pay != 0) throw new NotAcceptableException("Error occurred!");
        FlatEntity flatEntity = flatRepository.findById(buyHistoryEntity.getGood())
                .orElseThrow(() -> new DataNotFoundException("Flat not found!"));
        flatEntity.setOwner(buyHistoryEntity.getBuyer());
        if(Objects.equals(flatEntity.getAccommodation().getCompany().getOwner().getId(),buyHistoryEntity.getBuyer().getId()))
            flatEntity.setStatus(FlatStatus.AVAILABLE);
        else flatEntity.setStatus(FlatStatus.BUSY);
        systemBalanceEntity.setStatus(SystemBalanceStatus.EXPIRED);
        buyHistoryEntity.setStatus(BuyHistoryStatus.PAID);
        UserInboxEntity userInboxEntity1 = UserInboxEntity.builder()
                .state(MessageState.CLOSED)
                .type("TY")
                .fromWhom(buyHistoryEntity.getSeller())
                .toWhom(buyHistoryEntity.getBuyer())
                .message("Thank you for purchase!")
                .build();
        systemBalanceRepository.save(systemBalanceEntity);
        buyHistoryRepository.save(buyHistoryEntity);
        userInboxRepository.save(userInboxEntity1);
        userInboxRepository.save(userInboxEntity);
        flatRepository.save(flatEntity);
        return ApiResponse.builder()
                .message("Successfully approved")
                .status(HttpStatus.OK)
                .success(true)
                .build();
    }
    public ApiResponse rejectBooking(UUID inboxId) {
        UserInboxEntity userInboxEntity = userInboxRepository.findById(inboxId)
                .orElseThrow(() -> new DataNotFoundException("Inbox not found!"));
        userInboxEntity.setState(MessageState.CLOSED);
        UserEntity user = userInboxEntity.getFromWhom();
        SystemBalanceEntity systemBalanceEntity = systemBalanceRepository.findByUser(user)
                .orElseThrow(() -> new DataNotFoundException("System balance not found!"));
        BookingEntity bookingEntity = bookingRepository.findById(systemBalanceEntity.getForWhat())
                .orElseThrow(() -> new DataNotFoundException("Booking not found!"));
        bookingEntity.setStatus(BookingStatus.CLOSED);
        systemBalanceEntity.setStatus(SystemBalanceStatus.EXPIRED);
        systemBalanceRepository.save(systemBalanceEntity);
        userInboxRepository.save(userInboxEntity);
        bookingRepository.save(bookingEntity);
        return ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Successfully approve")
                .build();
    }

    public ApiResponse rejectBuy(UUID inboxId) {
        UserInboxEntity userInboxEntity = userInboxRepository.findById(inboxId)
                .orElseThrow(() -> new DataNotFoundException("Inbox not found!"));
        userInboxEntity.setState(MessageState.CLOSED);
        UserEntity user = userInboxEntity.getFromWhom();
        SystemBalanceEntity systemBalanceEntity = systemBalanceRepository.findByUser(user)
                .orElseThrow(() -> new DataNotFoundException("System balance not found!"));
        systemBalanceEntity.setStatus(SystemBalanceStatus.EXPIRED);
        systemBalanceRepository.save(systemBalanceEntity);
        userInboxRepository.save(userInboxEntity);
        return ApiResponse.builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Successfully approve")
                .build();
    }
}
