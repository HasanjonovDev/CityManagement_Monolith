package uz.pdp.city_management_monolith.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.city_management_monolith.domain.entity.user.UserEntity;
import uz.pdp.city_management_monolith.domain.entity.user.VerificationEntity;
import uz.pdp.city_management_monolith.exception.DataNotFoundException;
import uz.pdp.city_management_monolith.repository.user.UserRepository;
import uz.pdp.city_management_monolith.repository.user.VerificationRepository;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    @Value("${spring.mail.username}")
    private String sender;


    public void sendVerificationCode(UserEntity user) {
        VerificationEntity verificationEntity = verificationRepository.findVerificationEntityByUserId(user.getId())
                .orElseThrow(() -> new DataNotFoundException("Verification not found!"));
        String message = "This is your verification code to Business management service "
                + verificationEntity.getCode();

        UserEntity userEntity = userRepository.findUserEntityByEmail(user.getEmail()).orElseThrow(
                () -> new DataNotFoundException("User not found!"));

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Verification");
        simpleMailMessage.setTo(userEntity.getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    public void sendResetPassword(String email) {
        UserEntity userEntity = userRepository.findUserEntityByEmail(email).orElseThrow(
                () -> new DataNotFoundException("User not found!"));
        String message = "This is link to you to reset your password and you can change it!" +
                "\nhttp://localhost:8085/user/api/v1/auth/password-reset/" + email + "/" + userEntity.getRecoveryCode();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Reset Password");
        simpleMailMessage.setTo(userEntity.getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    public void send1ApprovedMessage(String email, Integer flatNumber) {
        String message = "Hey there is a book request to your flat № " + flatNumber + '\n'
                + "You can see it visiting our site!";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Book Request");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    public void saveCardMessage(String email, String number, Double Balance) {
        String message = "Your card Successfully added ✅" + '\n' +
                "Your card number💳 :" + number + '\n' +
                "Your balance💵 :" + Balance;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Payment");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    public void fillBalanceMessage(String email, String number, Double Balance) {
        String message = "Successfully incoming your balance ✅" + '\n' +
                "Your card number💳 :" + number + '\n' +
                "Your balance💵 :" + Balance;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Payment");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    public void receiverMessage(String email, Double money, String number) {
        String message = "Your card has been credited" + '\n' +
                "Sender card💳 :" + number + '\n' +
                "amount of money💵 :" + money;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Payment");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }


    public void senderMessage(String email, Double money, String number) {
        String message = "Money has been transferred from your card to another card" + '\n' +
                "Receiver card💳 :" + number + '\n' +
                "amount of money💵 :" + money;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Payment");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }
}
