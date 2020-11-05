package com.it.songservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class ArtistResponseDto {

    private Long id;

    private String name;

    private Set<GenreResponseDto> genres;

    private Set<String> notes;

}
