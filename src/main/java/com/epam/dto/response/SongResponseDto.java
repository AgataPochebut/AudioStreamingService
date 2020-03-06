package com.epam.dto.response;

import com.epam.model.Album;
import com.epam.model.Storage;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

public class SongResponseDto {

    private Long id;

    private String title;

    private Date year;

    private Set<String> notes;

    private AlbumResponseDto album;

    private StorageResponseDto storage;

}
