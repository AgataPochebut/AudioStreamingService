package com.epam.playservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class PlaylistResponseDto {

    private Long id;

    private String title;

    private Set<SongResponseDto> songs;

    private UserResponseDto user;

}
