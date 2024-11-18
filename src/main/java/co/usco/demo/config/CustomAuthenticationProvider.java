package co.usco.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationProvider;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import co.usco.demo.models.UserModel;
import co.usco.demo.services.UserService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String documentNumber = authentication.getName();
        String password = authentication.getCredentials().toString();
        String documentType = "cc"; // Pending to implement functionality to get the document type
        UserModel user = userService.findByDocumentNumberAndDocumentType(documentNumber, documentType);

        if (user == null) {
            throw new BadCredentialsException(getMessage("auth.documentInvalid"));
        }

        if (!user.isUserActive()) {
            throw new BadCredentialsException(getMessage("auth.unregisteredUser"));
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException(getMessage("auth.passwordInvalid"));
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());

        return new UsernamePasswordAuthenticationToken(documentNumber, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
