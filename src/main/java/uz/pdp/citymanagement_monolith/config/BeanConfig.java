package uz.pdp.citymanagement_monolith.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.AccommodationEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingEntity;
import uz.pdp.citymanagement_monolith.domain.entity.payment.CardEntity;
import uz.pdp.citymanagement_monolith.domain.entity.post.PostEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserInboxEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.VerificationEntity;

@Configuration
public class BeanConfig {
    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setSkipNullEnabled(true);
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
    @Bean
    public Class<?> domainClass() {return AccommodationEntity.class;}
    @Bean
    public Class<?> domainClassForCompany() {return CompanyEntity.class;}
    @Bean
    public Class<?> domainClassForBooking() {return BookingEntity.class;}
    @Bean
    public Class<?> domainClassForCard() {return CardEntity.class;}
    @Bean
    public Class<?> domainClassForRole() {return CardEntity.class;}
    @Bean
    public Class<?> domainClassForFlat() {return CardEntity.class;}
    @Bean
    public Class<?> domainClassForPermission() {return CardEntity.class;}
    @Bean
    public Class<?> domainClassForVerification() {return VerificationEntity.class;}
    @Bean
    public Class<?> domainClassForPost() {return PostEntity.class;}
    @Bean
    public Class<?> domainClassForUserInboxEntity() {return UserInboxEntity.class;}
}
