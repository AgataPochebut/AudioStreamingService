package com.epam.commonservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {

    private String name;

    @Override
    public String getAuthority() {
            return "ROLE_" + name.toUpperCase();
    }
}
