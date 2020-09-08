package com.it.songservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class AlbumResponseDto {

    private Long id;

    private String title;

    private Date year;

    private Set<String> notes;

    private Set<ArtistResponseDto> artists;

}
