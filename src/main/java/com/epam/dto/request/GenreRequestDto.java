package com.epam.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class GenreRequestDto {

    @NotNull
    @NotEmpty
    String name;

}
