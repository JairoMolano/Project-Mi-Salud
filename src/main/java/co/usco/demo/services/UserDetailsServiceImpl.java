package co.usco.demo.services;

import org.springframework.stereotype.Service;
import co.usco.demo.models.UserModel;
import co.usco.demo.repositories.UserRepository;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<SimpleGrantedAuthority> authoritirList = userModel.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toSet());

        return new User(userModel.getUsername(), userModel.getPassword(), authoritirList);
    }
}
