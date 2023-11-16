package uz.pdp.city_management_monolith.service.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.city_management_monolith.domain.dto.user.*;
import uz.pdp.city_management_monolith.domain.dto.response.ApiResponse;
import uz.pdp.city_management_monolith.domain.dto.response.JwtResponse;
import uz.pdp.city_management_monolith.domain.entity.apartment.AccommodationEntity;
import uz.pdp.city_management_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.city_management_monolith.domain.entity.apartment.FlatEntity;
import uz.pdp.city_management_monolith.domain.entity.user.UserEntity;
import uz.pdp.city_management_monolith.domain.entity.user.UserState;
import uz.pdp.city_management_monolith.domain.entity.user.VerificationEntity;
import uz.pdp.city_management_monolith.domain.filters.Filter;
import uz.pdp.city_management_monolith.exception.BadRequestException;
import uz.pdp.city_management_monolith.exception.DataNotFoundException;
import uz.pdp.city_management_monolith.exception.NotAcceptableException;
import uz.pdp.city_management_monolith.repository.apartment.AccommodationRepositoryImpl;
import uz.pdp.city_management_monolith.repository.apartment.CompanyRepositoryImpl;
import uz.pdp.city_management_monolith.repository.apartment.FlatRepositoryImpl;
import uz.pdp.city_management_monolith.repository.user.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepositoryImpl userRepository;
    private final CompanyRepositoryImpl companyRepository;
    private final FlatRepositoryImpl flatRepository;
    private final AccommodationRepositoryImpl accommodationRepository;
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

    public UserEntity getUser(String username) {
        return userRepository.findUserEntityByEmail(username)
                .orElseThrow(() -> new DataNotFoundException("User Not Found!"));
    }

    public UserEntity getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
    }

    public ApiResponse signUp(UserRequestDto userRequestDto) {
        ApiResponse apiResponse = checkEmailRegex(userRequestDto.getEmail());
        if (apiResponse != null)
            return apiResponse;
        UserEntity user = modelMapper.map(userRequestDto, UserEntity.class);
        if (checkEmail(user.getEmail())) throw new NotAcceptableException("Email already exists!");
        user.setState(UserState.UNVERIFIED);
        user.setAttempts(0);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRoles(List.of(roleRepository.findRoleEntityByRole("ROLE_USER").orElseThrow(() -> new DataNotFoundException("Role not found!"))));
        user.setRecoveryCode(String.valueOf(random.nextInt(10000, 100000)));
        UserEntity savedUser = userRepository.save(user);
        int i = random.nextInt(1000, 10000);
        verificationRepository.save(new VerificationEntity(user, (long) i));
        mailService.sendVerificationCode(savedUser);
        return ApiResponse.builder()
                .success(true)
                .message("Success")
                .status(HttpStatus.OK)
                .data(modelMapper.map(savedUser, UserDto.class))
                .build();
    }

    public ApiResponse login(LoginDto loginDto) {
        ApiResponse apiResponse = checkEmailRegex(loginDto.getEmail());
        if (apiResponse != null)
            return apiResponse;
        UserEntity user = userRepository.findUserEntityByEmail(loginDto.getEmail()).orElseThrow(
                () -> new DataNotFoundException("User not found!")
        );
        if (user.getState() == UserState.UNVERIFIED)
            throw new BadRequestException("Please verify your account!");
        if (user.getState() == UserState.BLOCKED)
            throw new BadRequestException("Your account has blocked!");
        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            JwtResponse build = JwtResponse.builder()
                    .accessToken(jwtService.generateAccessToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();
            return ApiResponse.builder()
                    .success(true)
                    .status(HttpStatus.OK)
                    .message("Success")
                    .data(build)
                    .build();
        }
        throw new BadRequestException("Wrong credentials!");
    }

    private JwtResponse loginWithEncoded(LoginDto loginDto) {
        UserEntity user = userRepository.findUserEntityByEmail(loginDto.getEmail()).orElseThrow(
                () -> new DataNotFoundException("User not found!")
        );
        if (Objects.equals(loginDto.getPassword(), user.getPassword())) {
            return JwtResponse.builder()
                    .accessToken(jwtService.generateAccessToken(user))
                    .refreshToken(jwtService.generateRefreshToken(user))
                    .build();
        }
        throw new BadRequestException("Wrong credentials!");
    }

    public ApiResponse resetPassword(String email) {
        mailService.sendResetPassword(email);
        return new ApiResponse(HttpStatus.OK, true, "Success");
    }

    public ApiResponse resetPassword(ResetPasswordDto resetPasswordDto, String email, String code) {
        UserEntity user = userRepository.findUserEntityByEmail(email).
                orElseThrow(() -> new DataNotFoundException("User do not exist"));
        if (!Objects.equals(user.getRecoveryCode(), code)) throw new BadRequestException("Invalid code!");
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
        if (user.getAttempts() != 3) {
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

    public ApiResponse changeName(Principal principal, String name) {
        UserEntity userNotFound = userRepository.findUserEntityByEmail(principal.getName())
                .orElseThrow(() -> new BadRequestException("User not found!"));
        userNotFound.setFirstName(name);
        UserEntity save = userRepository.save(userNotFound);
        return new ApiResponse(HttpStatus.OK, true, "success", save);
    }

    @Deprecated
    public List<UserDto> getDoctors() {
        return new ArrayList<>();
//        return userRepository.findUserEntitiesByRolesContaining(List.of(roleRepository.findRoleEntityByRole("DOCTOR").get()));
    }

    public UserResultDto details(UUID id) {
        UserEntity user = getUserById(id);
        List<CompanyEntity> companies = companyRepository.findCompanyEntitiesByOwnerId(id, new Filter());
        List<AccommodationEntity> accommodations = accommodationRepository.findByCompanyOwner(user);
        List<FlatEntity> flats = flatRepository.findByOwner(user);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return new UserResultDto(userDto, companies.size(), accommodations.size(), flats.size());
    }

    public List<UserForUserDto> getAll() {
        List<UserEntity> all = userRepository.findAll();
        List<UserForUserDto> forUserDto = new ArrayList<>();
        all.forEach((user) -> forUserDto.add(modelMapper.map(user, UserForUserDto.class)));
        return forUserDto;
    }

    public void block(UUID userId) {
        userRepository.changeState(userId, UserState.BLOCKED);
    }

    public void unblock(UUID userId) {
        userRepository.changeState(userId, UserState.ACTIVE);
    }

    public JwtResponse refreshToken(String refreshToken) {
        Jws<Claims> claimsJws = jwtService.extractToken(refreshToken);
        UserEntity userEntity = userRepository.findUserEntityByEmail(claimsJws.getBody().getSubject())
                .orElseThrow(() -> new DataNotFoundException("User not found!"));
        return JwtResponse.builder()
                .accessToken(jwtService.generateAccessToken(userEntity))
                .build();
    }

    private ApiResponse checkEmailRegex(String email) {
        List<String> message = new ArrayList<>();
        if (!email.contains("@")) message.add("Email should contain @ sign!");
        if (!email.contains(".")) message.add("Email should contain at least 1 dot sign!");
        if (!message.isEmpty())
            return ApiResponse.builder()
                    .message("Error")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST)
                    .data(message)
                    .build();
        if (email.substring(email.indexOf("@") + 1,
                email.lastIndexOf(".")).isEmpty()) {
            message.add("Invalid email");
        }
        if (email.substring(email.lastIndexOf(".") + 1).isEmpty()) {
            message.add("Invalid string after a dot!");
        }
        if (!message.isEmpty())
            return ApiResponse.builder()
                    .message("Error")
                    .success(false)
                    .status(HttpStatus.BAD_REQUEST)
                    .data(message)
                    .build();
        return null;
    }

    private Boolean checkEmail(String email) {
        Long l = userRepository.countUserEntitiesByEmail(email);
        return l >= 1;
    }
}
