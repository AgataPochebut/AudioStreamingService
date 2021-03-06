package com.it.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class GenreRequestDto {

    @NotNull
    @NotEmpty
    @Size(min = 2, max = 200)
    String name;

}
