package com.epam.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="ROLES")
public class Role extends BaseEntity implements GrantedAuthority {

    private String name;

    @Override
    public String getAuthority() {
            return "ROLE_" + name.toUpperCase();
    }
}
