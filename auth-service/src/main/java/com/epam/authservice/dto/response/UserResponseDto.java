package com.epam.authservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserResponseDto {

    private Long id;

    private String account;

    private String name;

    private Set<RoleResponseDto> roles;

}
