package com.epam.dto.response;

import com.epam.model.Genre;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class ArtistResponseDto {

    private Long id;

    private String name;

    private Set<String> notes;

    private Set<GenreResponseDto> genres;

}
