package uz.pdp.citymanagement_monolith.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.citymanagement_monolith.filter.JwtFilterToken;
import uz.pdp.citymanagement_monolith.service.user.AuthenticationService;
import uz.pdp.citymanagement_monolith.service.user.JwtService;
import uz.pdp.citymanagement_monolith.service.user.UserService;

@Configuration
@EnableMethodSecurity()
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserService auth;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final String[] roleCRUD ={"/user/role/save","/user/role/getRole","/user/role/{id}/updateRole","/user/role/{id}/deleteRole","/permissions/api/v1/**"};
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requestsConfigurer) ->
                        requestsConfigurer
                                .requestMatchers(roleCRUD).hasRole("SUPER_ADMIN")
                                .anyRequest().permitAll()
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtFilterToken(jwtService,authenticationService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder
                = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(auth)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
}
