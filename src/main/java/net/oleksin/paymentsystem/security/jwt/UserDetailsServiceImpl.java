package net.oleksin.paymentsystem.security.jwt;

import lombok.RequiredArgsConstructor;
import net.oleksin.paymentsystem.security.user.User;
import net.oleksin.paymentsystem.security.user.UserService;
import net.oleksin.paymentsystem.security.user.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username);

        return createUserDetails(user);
    }

    private UserDetails createUserDetails(User user) {
        return new JwtUserDetails(user, mapToGrantedAuthority(user.getRoles()));
    }

    private List<GrantedAuthority> mapToGrantedAuthority(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
    }
}
