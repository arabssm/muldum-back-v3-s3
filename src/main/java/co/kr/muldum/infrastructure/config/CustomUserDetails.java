package co.kr.muldum.infrastructure.config;

import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

public record CustomUserDetails(String userId) implements UserDetails {

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
