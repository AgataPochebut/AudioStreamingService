package com.epam.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class RoleRequestDto {

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private Date year;

    @NotNull
    @NotEmpty
    private  Set<String> notes;

    @NotNull
    @NotEmpty
    private Set<Long> artists;

}
