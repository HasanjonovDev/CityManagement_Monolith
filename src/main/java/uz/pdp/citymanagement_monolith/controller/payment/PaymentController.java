package uz.pdp.citymanagement_monolith.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.payment.CardDto;
import uz.pdp.citymanagement_monolith.domain.dto.payment.P2PDto;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.service.payment.PaymentService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/api/v1")
public class PaymentController {
    private final PaymentService paymentService;


    @PostMapping("/p2p")
    public ResponseEntity<CardEntity>p2p(
            @RequestBody P2PDto p2PDto
    ){
        return  ResponseEntity.ok(paymentService.peerToPeer(p2PDto));
    }
    @PostMapping("/card/save")
    public ResponseEntity<CardEntity> save(
            Principal principal,
            @RequestBody CardDto cardDto
    ){
        return ResponseEntity.ok(paymentService.saveCard(cardDto,principal));
    }
    @GetMapping("/card/get-b-user/{card}")
    public ResponseEntity<UUID> get(
            @PathVariable String card
    ) {
        return ResponseEntity.ok(paymentService.getUserByCard(card));
    }
    @GetMapping("/card/get/{cardId}")
    public ResponseEntity<String> getById(
            @PathVariable UUID cardId
    ) {
        return ResponseEntity.ok(paymentService.getById(cardId));
    }
    @GetMapping("/card/get")
    public ResponseEntity<List<CardEntity>>get(
            Principal principal
    ){
        return ResponseEntity.ok(paymentService.getCard(principal));
    }

    @PutMapping("/card/update/{id}")
    public ResponseEntity<CardEntity>update(
            @PathVariable UUID id,
            @RequestBody CardDto cardDto
    ){
        return ResponseEntity.ok(paymentService.updateCardById(id,cardDto));
    }


    @DeleteMapping("/card/delete/{id}")
    public ResponseEntity<HttpStatus>delete(
            @PathVariable UUID id
    ){
        paymentService.deleteCardById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/card/fill/{id}")
    public ResponseEntity<CardEntity>fill(
            @PathVariable UUID id,
            @RequestParam Double balance
    ){
        return ResponseEntity.ok(paymentService.fillBalance(id,balance));
    }
}