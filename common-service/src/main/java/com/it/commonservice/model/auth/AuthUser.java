package com.it.commonservice.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser implements OAuth2User {

    private Set<Authority> authorities;
    private Map<String, Object> attributes;
    private String nameAttributeKey;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return (String) attributes.get(nameAttributeKey);
    }
}
