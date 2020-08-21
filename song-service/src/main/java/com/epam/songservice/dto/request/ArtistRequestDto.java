package com.epam.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class ArtistRequestDto {

    @NotNull
    @NotEmpty
    private String name;

    private Set<Long> genres;

    private Set<String> notes;

}
