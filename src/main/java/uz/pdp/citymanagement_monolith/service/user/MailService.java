package uz.pdp.citymanagement_monolith.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.VerificationEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.user.UserRepository;
import uz.pdp.citymanagement_monolith.repository.user.VerificationRepository;

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
        String message = "This is link to you to reset your password and you can change it!\nhttp://localhost:8085/password-reset/" + email + "/1";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Reset Password");
        simpleMailMessage.setTo(userEntity.getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    public void send1ApprovedMessage(String email,Integer flatNumber) {
        String message = "Hey there is a book request to your flat № " + flatNumber+ '\n'
                + "You can approve it visiting our site!";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Book Request");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }
    public void send2ApprovedMessage(String email,Integer flatNumber) {
        String message = "Hey your book request to flat № " + flatNumber+ " was approved by its owner!\n"
                + "You can check it out and confirm your booking!";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Book Request");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }
    public void sendFullApproveMessageToCustomer(String email, FlatEntity flat) {
        String message = "Hey your booking is almost done!\nYou just booked a flat№ " + flat.getNumber() +
                " to " + flat.getPricePerMonth() + " a month! One you should do is to move there!";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Booking");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }
    public void sendFullApprovalToRenter(String email,String customerEmail,Integer flatNumber) {
        String message = "Hey, user: " + customerEmail + " just finished booking your flat№" +
                flatNumber + " so credits are being sent to your card and the customer is in the way to the flat!!\n" +
                "One more thing, please do not forget to cancel your post!";
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Booking");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }
    public void saveCardMessage(String email,String number,Double Balance) {
        String message = "Your card Successfully added ✅"+ '\n'+
                "Your card number💳 :" + number + '\n'+
                "Your balance💵 :" + Balance
                ;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Payment");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    public void fillBalanceMessage(String email,String number,Double Balance) {
        String message = "Successfully incoming your balance ✅"+ '\n'+
                "Your card number💳 :" + number + '\n'+
                "Your balance💵 :" + Balance
                ;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Payment");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }

    public void receiverMessage(String email, Double money, String number){
        String message= "Your card has been credited"+'\n'+
                "Sender card💳 :" +number + '\n' +
                "amount of money💵 :" + money;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Payment");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }


    public void senderMessage(String email, Double money, String number){
        String message= "Money has been transferred from your card to another card"+'\n'+
                "Receiver card💳 :" +number + '\n' +
                "amount of money💵 :" + money;
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("Payment");
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setText(message);
        javaMailSender.send(simpleMailMessage);
    }
}
