package com.it.songservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class SongResponseDto {

    private Long id;

    private String name;

    private Integer year;

    private Set<String> notes;

    private AlbumResponseDto album;

    private ResourceResponseDto resource;
}
