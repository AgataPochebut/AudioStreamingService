package com.epam.authservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RoleRequestDto {

    @NotNull
    @NotEmpty
    private String name;

}
