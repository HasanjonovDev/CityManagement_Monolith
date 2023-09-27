package uz.pdp.citymanagement_monolith.controller.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.payment.CardDto;
import uz.pdp.citymanagement_monolith.domain.dto.payment.CardForUserDto;
import uz.pdp.citymanagement_monolith.domain.dto.payment.P2PDto;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.payment.PaymentService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment/api/v1")
public class PaymentController {
    private final PaymentService paymentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/p2p")
    public ResponseEntity<CardForUserDto>p2p(
            @RequestBody P2PDto p2PDto
    ){
        return ResponseEntity.ok(paymentService.peerToPeer(p2PDto));
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/card/save")
    public ResponseEntity<CardForUserDto> save(

            Principal principal,
            @RequestBody CardDto cardDto
    ){
        return ResponseEntity.ok(paymentService.saveCard(cardDto,principal));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/card/get/{cardId}")
    public ResponseEntity<CardForUserDto> getById(
            @PathVariable UUID cardId
    ) {
        return ResponseEntity.ok(paymentService.getById(cardId));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/card/get")
    public ResponseEntity<List<CardForUserDto>>get(
            Principal principal,
            @RequestBody(required = false) Filter filter
    ){
        return ResponseEntity.ok(paymentService.getCard(principal,filter));
    }
    @PreAuthorize("hasAnyAuthority('PERMISSION_ALL_CRUD','PERMISSION_CARD_CRUD','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("/card/update/{id}")
    public ResponseEntity<CardForUserDto>update(
            @PathVariable UUID id,
            @RequestBody CardDto cardDto
    ){
        return ResponseEntity.ok(paymentService.updateCardById(id,cardDto));
    }

    @PreAuthorize("hasAnyAuthority('PERMISSION_ALL_CRUD','PERMISSION_CARD_CRUD','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("/card/delete/{id}")
    public ResponseEntity<HttpStatus>delete(
            @PathVariable UUID id
    ){
        paymentService.deleteCardById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('PERMISSION_ALL_CRUD','PERMISSION_CARD_CRUD','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("/card/fill/{id}")
    public ResponseEntity<CardForUserDto>fill(
            @PathVariable UUID id,
            @RequestParam Double balance
    ){
        return ResponseEntity.ok(paymentService.fillBalance(id,balance));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/card/{id}")
    public ResponseEntity<List<CardForUserDto>> getUserCards(
            @PathVariable UUID id,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(paymentService.getCard(id,filter));
    }
}
