package com.it.commonservice.service;

import com.it.commonservice.model.auth.AuthUser;
import com.it.commonservice.model.auth.Authority;

import java.util.Map;
import java.util.Set;

public class AuthUtil {

    public static AuthUser getGuest()
    {
        Map<String, Object> attributes = Map.of("name", "Guest");
        Set<Authority> roles = Set.of();

        return AuthUser.builder()
                .authorities(roles)
                .attributes(attributes)
                .nameAttributeKey("name")
                .build();
    }
}
