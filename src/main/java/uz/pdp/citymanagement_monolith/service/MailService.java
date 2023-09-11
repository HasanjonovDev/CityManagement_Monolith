package uz.pdp.citymanagement_monolith.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.entity.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.VerificationEntity;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.UserRepository;
import uz.pdp.citymanagement_monolith.repository.VerificationRepository;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    @Value("${spring.mail.username}")
    private String sender;
    private final Random random = new Random();

    public void sendVerificationCode(UserEntity user) {
        int i = random.nextInt(10000);
        VerificationEntity verificationEntity = verificationRepository.findVerificationEntityByUserId(user.getId()).orElseGet(
                () -> verificationRepository.save(new VerificationEntity("http://localhost:8080/user/api/v1/auth/verify/" + user.getId() + "/" + i, user, (long) i))
        );
        String message = "This is your verification code to Business management service "
                + verificationEntity.getCode() + "\nThis code will be expired in 10 minutes.\nUse this link to verify "
                + verificationEntity.getLink();

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
        String message = "This is link to you to reset your password and you can change it!\nhttp://localhost:8080/user/api/v1/auth/changePassword";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Reset Password");
        simpleMailMessage.setTo(userEntity.getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }
}
