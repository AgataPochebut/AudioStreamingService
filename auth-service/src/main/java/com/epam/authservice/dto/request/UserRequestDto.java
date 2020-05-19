package com.epam.authservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserRequestDto {

    @NotNull
    @NotEmpty
    private String account;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private  Set<Long> roles;

}
