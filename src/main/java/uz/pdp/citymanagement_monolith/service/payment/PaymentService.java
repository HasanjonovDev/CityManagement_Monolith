package uz.pdp.citymanagement_monolith.service.payment;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.payment.CardDto;
import uz.pdp.citymanagement_monolith.domain.dto.payment.P2PDto;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardType;
import uz.pdp.citymanagement_monolith.exception.BadRequestException;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.payment.CardRepository;
import uz.pdp.citymanagement_monolith.service.MailService;
import uz.pdp.citymanagement_monolith.service.UserService;
import uz.pdp.citymanagement_monolith.service.apartment.FlatService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final MailService mailService;
    private final FlatService flatService;

    public CardEntity saveCard(CardDto cardDto, Principal principal){
        CardEntity card = modelMapper.map(cardDto, CardEntity.class);
        UserEntity user = userService.getUser(principal.getName());
        try {
            card.setType(CardType.valueOf(cardDto.getType()));
        } catch (Exception e) {
            throw new BadRequestException("Invalid type!");
        }
        card.setOwnerId(user.getId());
        card.setBalance(10000000.0);
        mailService.saveCardMessage(user.getEmail(),card.getNumber(),card.getBalance());
        return cardRepository.save(card);
    }

    public List<CardEntity>getCard(Principal  principal){
        UserEntity user = userService.getUser(principal.getName());
        return cardRepository.findCardEntitiesByOwnerId(user.getId());
    }

    public void deleteCardById(UUID cardId){
        cardRepository.deleteById(cardId);
    }


    public CardEntity updateCardById(UUID id,CardDto cardDto){
        CardEntity card = cardRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Card Not found"));
        modelMapper.map(cardDto,card);
        return cardRepository.save(card);
    }

    public CardEntity fillBalance(UUID cardId,Double balance) {
        CardEntity card = cardRepository.getReferenceById(cardId);
        card.setBalance(balance);
        UserEntity user = userService.getUserById(card.getOwnerId());
        mailService.fillBalanceMessage(user.getEmail(), card.getNumber(), card.getBalance());
        return cardRepository.save(card);
    }

    public CardEntity peerToPeer(P2PDto p2PDto){
        CardEntity senderCard = cardRepository.findCardEntityByNumber(p2PDto.getSender())
                .orElseThrow(()->new DataNotFoundException("Card not found"));
        CardEntity receiverCard= cardRepository.findCardEntityByNumber(p2PDto.getReceiver())
                .orElseThrow(()->new DataNotFoundException("Card not found"));
        UserEntity senderUser = userService.getUserById(senderCard.getOwnerId());
        UserEntity receiverUser = userService.getUserById(receiverCard.getOwnerId());
        if (senderCard.getBalance()< p2PDto.getCash()){
            throw new BadRequestException("Insufficient funds");
        }

        senderCard.setBalance(senderCard.getBalance()- p2PDto.getCash());
        receiverCard.setBalance(receiverCard.getBalance()+ p2PDto.getCash());

        mailService.receiverMessage(receiverUser.getEmail(), receiverCard.getBalance(), senderCard.getNumber());
        mailService.senderMessage(senderUser.getEmail(),senderCard.getBalance(),receiverCard.getNumber());

        cardRepository.save(receiverCard);
        return cardRepository.save(senderCard);
    }

    public UUID getUserByCard(String card) {
        return cardRepository.findCardEntityByNumber(card).orElseThrow(() -> new DataNotFoundException("Card not found")).getOwnerId();
    }

    public String getById(UUID cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new DataNotFoundException("Card not found!")).getNumber();
    }
    public void pay(String senderCardNumber,UUID receiverFlatId,Double amount) {
        UUID receiverCardId = flatService.getFlat(receiverFlatId).getCardId();
        String receiverCard = getById(receiverCardId);
        P2PDto paymentDto = P2PDto.builder()
                .sender(senderCardNumber)
                .receiver(receiverCard)
                .cash(amount)
                .build();

        peerToPeer(paymentDto);
    }
}
