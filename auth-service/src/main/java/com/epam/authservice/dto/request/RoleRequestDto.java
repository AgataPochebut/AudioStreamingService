package com.epam.authservice.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

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
