package my.jmangeruga.recipes.adapter.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import static java.util.Objects.requireNonNull;

final public class User implements UserDetails {
    private static final long serialVersionUID = 2396654715019746670L;
    private static final String NO_USER_NAME = "";
    private static final String NO_PASSWORD = "";

    private final String id;

    User(String id) {
        this.id = requireNonNull(id);
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return NO_PASSWORD;
    }

    @Override
    public String getUsername() {
        return NO_USER_NAME;
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
