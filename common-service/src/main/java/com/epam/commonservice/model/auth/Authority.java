package com.epam.commonservice.model.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authority implements GrantedAuthority {

    private String name;

    @Override
    public String getAuthority() {
            return "ROLE_" + name.toUpperCase();
    }
}
