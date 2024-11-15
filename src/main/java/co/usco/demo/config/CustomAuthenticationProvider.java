package co.usco.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import co.usco.demo.models.RoleModel;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String documentNumber = authentication.getName(); // Esto será el número de documento
        String password = authentication.getCredentials().toString();

        // Buscar el usuario por documentNumber en lugar de username
        UserModel user = userRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Verificar la contraseña
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Incorrect password") {};
        }

        // Obtener los roles
        Set<RoleModel> roles = user.getRoles();

        // Convertir roles a GrantedAuthority
        Set<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());

        // Retornar un token de autenticación exitoso
        return new UsernamePasswordAuthenticationToken(documentNumber, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
