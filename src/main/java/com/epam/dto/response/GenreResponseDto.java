package com.epam.dto.response;

import java.util.Date;
import java.util.Set;

public class GenreResponseDto {

    private Long id;

    String name;

    public static class PlaylistResponseDto {

        private Long id;

        private String title;

        private Date year;

        private Set<String> notes;

    }
}
