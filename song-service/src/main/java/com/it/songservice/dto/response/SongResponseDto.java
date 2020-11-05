package com.it.songservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class SongResponseDto {

    private Long id;

    private String name;

    private AlbumResponseDto album;

    private Set<String> notes;

}
