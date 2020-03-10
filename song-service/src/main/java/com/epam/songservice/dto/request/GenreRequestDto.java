package com.epam.songservice.dto.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class GenreRequestDto {

    @NotNull
    @NotEmpty
    String name;

}
