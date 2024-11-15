package co.usco.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import co.usco.demo.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // SECURITY FILTER CHAIN
    @Bean
    @SuppressWarnings("deprecation")
       public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
        .authenticationProvider(authenticationProvider)
        .authorizeRequests(auth -> auth 
            .anyRequest().permitAll())
            .formLogin(login -> login
            .permitAll()
            .successHandler(authenticationSuccessHandler()))
            .logout(logout -> logout
            .permitAll()
            .logoutSuccessUrl("/login"));
        return http.build();
    }

    // AUTHENTICATION MANAGER
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // AUTHENTICATION PROVIDER
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsServiceImpl) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsServiceImpl);
        return provider;
    }

    // PASSWORD ENCODER
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AUTHENTICATION SUCCESS HANDLER
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

    // AUTHENTICATION FAILURE HANDLER
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            request.getSession().invalidate();
            response.sendRedirect("redirect:/login");
        };
    }

}