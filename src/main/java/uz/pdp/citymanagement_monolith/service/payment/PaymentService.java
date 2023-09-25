package uz.pdp.citymanagement_monolith.service.payment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.payment.CardDto;
import uz.pdp.citymanagement_monolith.domain.dto.payment.CardForUserDto;
import uz.pdp.citymanagement_monolith.domain.dto.payment.P2PDto;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardType;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.BadRequestException;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.apartment.FlatRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.payment.CardRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserRepositoryImpl;
import uz.pdp.citymanagement_monolith.service.user.MailService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final CardRepositoryImpl cardRepository;
    private final UserRepositoryImpl userRepository;
    private final FlatRepositoryImpl flatRepository;
    private final ModelMapper modelMapper;
    private final MailService mailService;

    public CardForUserDto saveCard(CardDto cardDto, Principal principal){
        CardEntity card = modelMapper.map(cardDto, CardEntity.class);
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        card.setExpiredDate(cardDto.getExpireDate());
        try {
            card.setType(CardType.valueOf(cardDto.getType()));
        } catch (Exception e) {
            throw new BadRequestException("Invalid type!");
        }
        card.setOwner(user);
        card.setBalance(0.0);
        mailService.saveCardMessage(user.getEmail(),card.getNumber(),card.getBalance());
        return modelMapper.map(cardRepository.save(card),CardForUserDto.class);
    }

    public List<CardForUserDto> getCard(Principal principal, Filter filter){
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        List<CardEntity> cards = cardRepository.findCardEntitiesByOwnerId(user.getId(), filter);
        List<CardForUserDto> cardsForUser = new ArrayList<>();
        cards.forEach((cardEntity -> cardsForUser.add(modelMapper.map(cardEntity, CardForUserDto.class))));
        return cardsForUser;
    }

    public void deleteCardById(UUID cardId){
        cardRepository.deleteById(cardId);
    }


    public CardForUserDto updateCardById(UUID id,CardDto cardDto){
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Card Not found"));
        modelMapper.map(cardDto,card);
        return modelMapper.map(cardRepository.save(card),CardForUserDto.class);
    }

    public CardForUserDto fillBalance(UUID cardId,Double balance) {
        CardEntity card = cardRepository.getReferenceById(cardId);
        card.setBalance(balance);
        UserEntity user = card.getOwner();
        mailService.fillBalanceMessage(user.getEmail(), card.getNumber(), card.getBalance());
        return modelMapper.map(cardRepository.save(card),CardForUserDto.class);
    }

    public CardForUserDto peerToPeer(P2PDto p2PDto){
        CardEntity senderCard = cardRepository.findCardEntityByNumber(p2PDto.getSender())
                .orElseThrow(()->new DataNotFoundException("Card not found"));
        CardEntity receiverCard= cardRepository.findCardEntityByNumber(p2PDto.getReceiver())
                .orElseThrow(()->new DataNotFoundException("Card not found"));
        UserEntity senderUser = senderCard.getOwner();
        UserEntity receiverUser = receiverCard.getOwner();
        if (senderCard.getBalance()< p2PDto.getCash()){
            throw new BadRequestException("Insufficient funds");
        }

        senderCard.setBalance(senderCard.getBalance()- p2PDto.getCash());
        receiverCard.setBalance(receiverCard.getBalance()+ p2PDto.getCash());

        mailService.receiverMessage(receiverUser.getEmail(), receiverCard.getBalance(), senderCard.getNumber());
        mailService.senderMessage(senderUser.getEmail(),senderCard.getBalance(),receiverCard.getNumber());

        cardRepository.save(receiverCard);
        return modelMapper.map(cardRepository.save(senderCard),CardForUserDto.class);
    }

    public UUID getUserByCard(String card) {
        return cardRepository.findCardEntityByNumber(card).orElseThrow(() -> new DataNotFoundException("Card not found")).getOwner().getId();
    }

    public String getById(UUID cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new DataNotFoundException("Card not found!")).getNumber();
    }
    @Deprecated
    public void pay(String senderCardNumber,UUID receiverFlatId,Double amount) {
        UUID receiverCardId = flatRepository.getFlatCardId(receiverFlatId);
        String receiverCard = getById(receiverCardId);
        P2PDto paymentDto = P2PDto.builder()
                .sender(senderCardNumber)
                .receiver(receiverCard)
                .cash(amount)
                .build();

        peerToPeer(paymentDto);
    }

    public List<CardEntity> getCardByOwnerId(UUID id){
        return List.of(cardRepository.findCardEntityByOwnerId(id).orElseThrow(
                ()->new DataNotFoundException("Card not found")));
    }
}
