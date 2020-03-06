package com.epam.dto.request;

import com.epam.model.Genre;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class ArtistRequestDto {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private Set<String> notes;

    @NotNull
    @NotEmpty
    private Set<Long> genres;

}
