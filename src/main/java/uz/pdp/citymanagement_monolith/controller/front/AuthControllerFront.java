package uz.pdp.citymanagement_monolith.controller.front;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import uz.pdp.citymanagement_monolith.domain.dto.LoginDto;
import uz.pdp.citymanagement_monolith.domain.dto.UserRequestDto;
import uz.pdp.citymanagement_monolith.domain.dto.reader.VerificationDto;
import uz.pdp.citymanagement_monolith.domain.entity.UserEntity;
import uz.pdp.citymanagement_monolith.exception.MyException;
import uz.pdp.citymanagement_monolith.service.front.UserServiceFront;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class AuthControllerFront {
    private final UserServiceFront userService;
    @PostMapping("/login")
    public String login(
            LoginDto loginDto,
            Model model
    ) {
        try {
            userService.login(loginDto);
        } catch (MyException e) {
            model.addAttribute("message",e.getMessage());
            return "index";
        }
        return "MainPage";
    }
    @GetMapping ("/sign-up")
    public String signUp() {
        return "/auth/signUp";
    }
    @PostMapping("/sign-up")
    public String signUp(UserRequestDto userRequestDto, BindingResult bindingResult, Model model){
        UserEntity userEntity = userService.signUp(userRequestDto, bindingResult);
        model.addAttribute("userId",userEntity.getId());
        return "/auth/VerificationPage";
    }
    @GetMapping("/verify/{userId}")
    public String verifyGet(@PathVariable UUID userId,Model model)  {
        model.addAttribute("userId",userId);
        return "/auth/VerificationPage";
    }
    @PostMapping("/verify")
    public String verify(VerificationDto verificationDto) {
        userService.verify(verificationDto);
        return "MainPage";
    }
}
