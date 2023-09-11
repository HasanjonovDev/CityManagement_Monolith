package uz.pdp.citymanagement_monolith.service.front;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import uz.pdp.citymanagement_monolith.domain.dto.LoginDto;
import uz.pdp.citymanagement_monolith.domain.dto.UserRequestDto;
import uz.pdp.citymanagement_monolith.domain.dto.reader.VerificationDto;
import uz.pdp.citymanagement_monolith.domain.dto.response.JwtResponse;
import uz.pdp.citymanagement_monolith.domain.entity.JwtTokenEntity;
import uz.pdp.citymanagement_monolith.domain.entity.UserEntity;
import uz.pdp.citymanagement_monolith.exception.ExceptionFront;
import uz.pdp.citymanagement_monolith.exception.MyException;
import uz.pdp.citymanagement_monolith.repository.JwtTokenRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceFront {
    private final JwtTokenRepository jwtTokenRepository;
    private final uz.pdp.citymanagement_monolith.service.UserService userService;


    public void login(LoginDto loginDto) {
        if ((loginDto.getEmail() == null || loginDto.getEmail().isBlank())
                && (loginDto.getPassword() == null || loginDto.getPassword().isBlank())) {
            throw new MyException("Email and password is missing!");
        } else if (loginDto.getEmail() == null || loginDto.getEmail().isBlank()) {
            throw new MyException("Email is missing!");
        } else if (loginDto.getPassword() == null || loginDto.getPassword().isBlank()) {
            throw new MyException("Password is missing!");
        }
        JwtResponse login = userService.login(loginDto);
        JwtTokenEntity token = JwtTokenEntity.builder()
                .username(loginDto.getEmail())
                .token(login.getAccessToken())
                .build();
        jwtTokenRepository.save(token);
    }

    public UserEntity signUp(UserRequestDto userRequestDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) throw new ExceptionFront(bindingResult.getAllErrors());
        return userService.signUp(userRequestDto);
    }

    public void verify(VerificationDto verificationDto) {
        JwtResponse data = (JwtResponse) userService.verify(verificationDto.getUserId(), verificationDto.getCode()).getData();
        UserEntity user = userService.getUserById(verificationDto.getUserId());
        jwtTokenRepository.save(JwtTokenEntity.builder().username(user.getEmail()).token(data.getAccessToken()).build());
    }
    public UserEntity getUserById(UUID id) {
        return userService.getUserById(id);
    }
}
