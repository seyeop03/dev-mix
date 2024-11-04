package msa.devmix.config.oauth.userinfo;

import lombok.Getter;
import msa.devmix.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Getter
public class UserPrincipal implements UserDetails, OAuth2User {

    private User user;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserPrincipal(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    public UserPrincipal(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public static UserPrincipal from(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
        return new UserPrincipal(
                user,
                authorities
        );
    }

    /**
     * UserDetails 오버라이딩
     */
    // 사용자의 id를 반환(고유값)
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // true -> 만료되지 않음
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // true -> 잠금되지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // true -> 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        return true; // 사용 가능
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * OAuth2User 오버라이딩
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }
}
