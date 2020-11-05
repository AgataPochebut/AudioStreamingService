package com.it.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
public class ArtistRequestDto {

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 200)
    private String name;

    private Set<Long> genres;

    private Set<String> notes;

}
