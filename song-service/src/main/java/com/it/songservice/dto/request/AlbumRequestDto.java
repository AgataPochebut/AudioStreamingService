package com.it.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
public class AlbumRequestDto {

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 200)
    private String name;

    private Integer year;

    private Set<Long> artists;

    private Set<Long> genres;

    private  Set<String> notes;

}
