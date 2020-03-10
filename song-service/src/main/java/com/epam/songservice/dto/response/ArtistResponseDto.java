package com.epam.songservice.dto.response;

import java.util.Set;

public class ArtistResponseDto {

    private Long id;

    private String name;

    private Set<String> notes;

    private Set<GenreResponseDto> genres;

}
