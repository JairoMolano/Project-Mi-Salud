package co.usco.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private CustomAuthenticationFilter customAuthenticationFilter;

    @Bean
    @SuppressWarnings("deprecation")
       public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
        .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .authenticationProvider(customAuthenticationProvider())
        .authorizeRequests(auth -> auth 
            .requestMatchers("/change-language").permitAll()
            .requestMatchers("/home/**").permitAll()
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/static/**", "/img/**", "/styles/**").permitAll()
            .requestMatchers("/common/change-language").permitAll()
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
            .successHandler(authenticationSuccessHandler())
            .failureHandler(authenticationFailureHandler()))
            .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/auth/login?logout")
            .invalidateHttpSession(true)
            .permitAll());
        return http.build();
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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

    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String errorMessage;
            if (exception instanceof BadCredentialsException || exception instanceof DisabledException) {
                errorMessage = exception.getMessage();
            } else {
                errorMessage = messageSource.getMessage("auth.somethingWentWrong", null, LocaleContextHolder.getLocale());
            }
            request.getSession().setAttribute("error", errorMessage);
            response.sendRedirect("/auth/login");
        };
    }

}
