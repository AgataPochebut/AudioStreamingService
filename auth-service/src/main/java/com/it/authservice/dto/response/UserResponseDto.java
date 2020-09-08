package com.it.authservice.dto.response;

import com.it.authservice.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserResponseDto {

    private Long id;

    private String account;

    private Set<Role> roles;

}
