package uz.pdp.citymanagement_monolith.service.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.user.LoginDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.ResetPasswordDto;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserRequestDto;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.domain.dto.response.JwtResponse;
import uz.pdp.citymanagement_monolith.domain.dto.user.UserDto;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserState;
import uz.pdp.citymanagement_monolith.domain.entity.user.VerificationEntity;
import uz.pdp.citymanagement_monolith.exception.BadRequestException;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.exception.NotAcceptableException;
import uz.pdp.citymanagement_monolith.repository.user.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepositoryImpl userRepository;
    private final VerificationRepositoryImpl verificationRepository;
    private final RoleRepositoryImpl roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final MailService mailService;
    private final JwtService jwtService;
    private final Random random = new Random();
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserEntityByEmail(username).orElseThrow(
                () -> new DataNotFoundException("User not found!")
        );
    }

    public UserEntity getUser(String username){
        return userRepository.findUserEntityByEmail(username)
                .orElseThrow(() -> new DataNotFoundException("User Not Found!"));
    }

    public UserEntity getUserById(UUID id){
        return userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
    }
    public UserDto signUp(UserRequestDto userRequestDto) {
        UserEntity user = modelMapper.map(userRequestDto, UserEntity.class);
        if(checkEmail(user.getEmail())) throw new NotAcceptableException("Email already exists!");
        user.setState(UserState.UNVERIFIED);
        user.setAttempts(0);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRoles(List.of(roleRepository.findRoleEntityByRole("ROLE_USER").orElseThrow(() -> new DataNotFoundException("Role not found!"))));
        UserEntity savedUser = userRepository.save(user);
        int i = random.nextInt(1000,10000);
        verificationRepository.save(new VerificationEntity(user, (long) i));
        mailService.sendVerificationCode(savedUser);
        return modelMapper.map(savedUser,UserDto.class);
    }
    private Boolean checkEmail(String email) {
        Long l = userRepository.countUserEntitiesByEmail(email);
        return l >= 1;
    }

    public JwtResponse login(LoginDto loginDto) {
        UserEntity user = userRepository.findUserEntityByEmail(loginDto.getEmail()).orElseThrow(
                () -> new DataNotFoundException("User not found!")
        );
        if(passwordEncoder.matches(loginDto.getPassword(),user.getPassword())) {
            return JwtResponse.builder()
                    .accessToken(jwtService.generateAccessToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();
        }
        throw new BadRequestException("Wrong credentials!");
    }
    private JwtResponse loginWithEncoded(LoginDto loginDto) {
        UserEntity user = userRepository.findUserEntityByEmail(loginDto.getEmail()).orElseThrow(
                () -> new DataNotFoundException("User not found!")
        );
        if(Objects.equals(loginDto.getPassword(),user.getPassword())) {
            return JwtResponse.builder()
                    .accessToken(jwtService.generateAccessToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();
        }
        throw new BadRequestException("Wrong credentials!");
    }
    public ApiResponse resetPassword(String email) {
        mailService.sendResetPassword(email);
        return new ApiResponse(HttpStatus.OK,true,"Success");
    }
    public ApiResponse resetPassword(ResetPasswordDto resetPasswordDto,String email) {
        UserEntity user = userRepository.findUserEntityByEmail(email).
                orElseThrow(() -> new DataNotFoundException("User do not exist"));
        if (!Objects.equals(resetPasswordDto.getNewPassword(), resetPasswordDto.getConfirmPassword())) {
            throw new NotAcceptableException("Both passwords must be same");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
        userRepository.save(user);
        return new ApiResponse(HttpStatus.OK, true, "success");
    }


    public ApiResponse verify(UUID userId, String code) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User Not found"));
        VerificationEntity verificationEntity = verificationRepository.findVerificationEntityByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("Verification code not found"));
        if (user.getAttempts()!=3) {
            if (Objects.equals(code, verificationEntity.getCode().toString())) {
                if (verificationEntity.getCreatedTime().plusMinutes(10).isAfter(LocalDateTime.now())) {
                    verificationRepository.delete(verificationEntity);
                    user.setState(UserState.ACTIVE);
                    UserEntity save = userRepository.save(user);
                    JwtResponse login = loginWithEncoded(LoginDto.builder().email(save.getEmail()).password(save.getPassword()).build());
                    return ApiResponse.builder().status(HttpStatus.OK).data(login).message("Successfully verified").success(true).build();
                }
                throw new NotAcceptableException("Verification Code Expired!");
            }
            user.setAttempts(user.getAttempts() + 1);
            userRepository.save(user);
            throw new NotAcceptableException("Wrong Verification Code!");
        }
        user.setState(UserState.BLOCKED);
        userRepository.save(user);
        throw new NotAcceptableException("Too many failed attempts. You have been blocked!");
    }
    public ApiResponse changeName(Principal principal,String name){
        UserEntity userNotFound = userRepository.findUserEntityByEmail(principal.getName()).orElseThrow(() -> new DataNotFoundException("User not found"));
        userNotFound.setName(name);
        UserEntity save = userRepository.save(userNotFound);
        return new ApiResponse(HttpStatus.OK,true,"success",save);
    }

    public List<UserDto> getDoctors(){
        return new ArrayList<>();
//        return userRepository.findUserEntitiesByRolesContaining(List.of(roleRepository.findRoleEntityByRole("DOCTOR").get()));
    }
}
