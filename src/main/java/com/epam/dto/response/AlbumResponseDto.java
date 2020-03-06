package com.epam.dto.response;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

public class AlbumResponseDto {

    private Long id;

    private String title;

    private Date year;

    private Set<String> notes;

    private Set<ArtistResponseDto> artists;

}
