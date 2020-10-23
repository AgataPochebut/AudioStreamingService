package com.it.playservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class PlaylistResponseDto {

    private Long id;

    private String name;

    private Set<SongResponseDto> songs;

    private UserResponseDto user;

}
