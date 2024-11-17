package co.usco.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import co.usco.demo.models.UserModel;
import co.usco.demo.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // Get username (docuemntNumber) and password
        String documentNumber = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Get documentType from request
        String documentType = getDocumentTypeFromRequest();

        // Search user in the database
        UserModel user = userService.findByDocumentNumberAndDocumentType(documentNumber, documentType);


        // Verify if the user is active
        if (!user.isUserActive()) {
            throw new DisabledException("User is not active");
        }

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        // Convert roles to authorities
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());

        // Return authenticated user
        return new UsernamePasswordAuthenticationToken(documentNumber, password, authorities);
    }

    private String getDocumentTypeFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getParameter("documentType");
        }
        throw new AuthenticationServiceException("Document type not found in the request");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
