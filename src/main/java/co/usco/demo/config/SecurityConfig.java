package co.usco.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Security Filter Chain
    @Bean
    @SuppressWarnings("deprecation")
       public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
        .authenticationProvider(customAuthenticationProvider())
        .authorizeRequests(auth -> auth 
            .requestMatchers("/home/**").permitAll()
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/static/**", "/img/**", "/styles/**").permitAll()
            .requestMatchers("/patient/**").hasAnyAuthority("ROLE_PATIENT")
            .requestMatchers("/support-staff/**").hasAnyAuthority("ROLE_SUPPORT_STAFF")
            .requestMatchers("/medical-staff/**").hasAnyAuthority("ROLE_MEDICAL_STAFF")
            .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
            .anyRequest().authenticated())
            .formLogin(login -> login
            .loginPage("/auth/login")
            .usernameParameter("documentNumber")
            .loginProcessingUrl("/auth/login")
            .permitAll()
            .successHandler(authenticationSuccessHandler()))
            .logout(logout -> logout
            .permitAll()
            .logoutSuccessUrl("/auth/login"));
        return http.build();
    }

    // Custom Authentication Provider
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Success Handler
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            authentication.getAuthorities().forEach(authority -> {
                try {
                    switch (authority.getAuthority()) {
                        case "ROLE_PATIENT":
                            response.sendRedirect("/patient/dashboard");
                            break;
                        case "ROLE_SUPPORT_STAFF":
                            response.sendRedirect("/support-staff/dashboard");
                            break;
                        case "ROLE_MEDICAL_STAFF":
                            response.sendRedirect("/medical-staff/dashboard");
                            break;
                        case "ROLE_ADMIN":
                            response.sendRedirect("/admin/dashboard");
                            break;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        };
    }

    // Authentication Failure Handler
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            request.getSession().invalidate();
            response.sendRedirect("redirect:/auth/login");
        };
    }

}