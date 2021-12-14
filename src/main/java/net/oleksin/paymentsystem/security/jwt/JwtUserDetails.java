package net.oleksin.paymentsystem.security.jwt;

import lombok.Data;
import net.oleksin.paymentsystem.security.user.User;
import net.oleksin.paymentsystem.security.user.role.Status;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class JwtUserDetails implements UserDetails {
    private final User user;
    private final List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return Status.ACTIVE.equals(user.getStatus());
    }

    @Override
    public boolean isAccountNonLocked() {
        return Status.ACTIVE.equals(user.getStatus());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return Status.ACTIVE.equals(user.getStatus());
    }

    @Override
    public boolean isEnabled() {
        return Status.ACTIVE.equals(user.getStatus());
    }
}
