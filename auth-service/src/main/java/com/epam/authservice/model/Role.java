package com.epam.authservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
@Data
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @Override
    public String getAuthority() {
            return "ROLE_" + name.toUpperCase();
    }
}
