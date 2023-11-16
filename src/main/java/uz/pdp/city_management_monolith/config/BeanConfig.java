package uz.pdp.city_management_monolith.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import uz.pdp.city_management_monolith.domain.dto.response.LocalDateTimeToDateConverter;
import uz.pdp.city_management_monolith.domain.entity.apartment.AccommodationEntity;
import uz.pdp.city_management_monolith.domain.entity.apartment.CompanyEntity;
import uz.pdp.city_management_monolith.domain.entity.booking.BookingDaysStatusEntity;
import uz.pdp.city_management_monolith.domain.entity.booking.BookingEntity;
import uz.pdp.city_management_monolith.domain.entity.booking.BuyHistoryEntity;
import uz.pdp.city_management_monolith.domain.entity.booking.PreOrderBookingEntity;
import uz.pdp.city_management_monolith.domain.entity.payment.CardEntity;
import uz.pdp.city_management_monolith.domain.entity.post.PostEntity;
import uz.pdp.city_management_monolith.domain.entity.system.SystemBalanceEntity;
import uz.pdp.city_management_monolith.domain.entity.user.UserInboxEntity;
import uz.pdp.city_management_monolith.domain.entity.user.VerificationEntity;

@Configuration
public class BeanConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setSkipNullEnabled(true);
        modelMapper.addConverter(new LocalDateTimeToDateConverter());
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Class<?> domainClass() {
        return AccommodationEntity.class;
    }

    @Bean
    public Class<?> domainClassForCompany() {
        return CompanyEntity.class;
    }

    @Bean
    public Class<?> domainClassForBooking() {
        return BookingEntity.class;
    }

    @Bean
    public Class<?> domainClassForCard() {
        return CardEntity.class;
    }

    @Bean
    public Class<?> domainClassForRole() {
        return CardEntity.class;
    }

    @Bean
    public Class<?> domainClassForFlat() {
        return CardEntity.class;
    }

    @Bean
    public Class<?> domainClassForPermission() {
        return CardEntity.class;
    }

    @Bean
    public Class<?> domainClassForVerification() {
        return VerificationEntity.class;
    }

    @Bean
    public Class<?> domainClassForPost() {
        return PostEntity.class;
    }

    @Bean
    public Class<?> domainClassForUserInboxEntity() {
        return UserInboxEntity.class;
    }

    @Bean
    public Class<?> domainClassForSystemBalance() {
        return SystemBalanceEntity.class;
    }

    @Bean
    public Class<?> domainClassForBuyHistory() {
        return BuyHistoryEntity.class;
    }

    @Bean
    public Class<?> domainClassForPreOrders() {
        return PreOrderBookingEntity.class;
    }

    @Bean
    public Class<?> domainClassForBookingDayStatus() {
        return BookingDaysStatusEntity.class;
    }
}
