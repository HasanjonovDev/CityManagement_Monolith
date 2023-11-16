package uz.pdp.city_management_monolith;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Rental management",
                version = "1.0.1",
                description = "This project is created to simplify all rents of real estate",
                termsOfService = "https://www.termsfeed.com/live/5f5eb12b-08aa-489a-97b7-3bf6fe999215",
                contact = @Contact(
                        name = "Support",
                        email = "rentmanagement.teenagers@gmail.com"
                ),
                license = @License(
                        name = "Licence",
                        url = "http://localhost:8080/base"
                )
        )
)
@SecurityScheme(
        name = "jwtBearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class CityManagementMonolithApplication {
    public static void main(String[] args) {
        SpringApplication.run(CityManagementMonolithApplication.class, args);
    }

}
