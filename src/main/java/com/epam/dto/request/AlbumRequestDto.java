package com.epam.dto.request;

import com.epam.model.Artist;
import com.epam.model.Tag;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

public class AlbumRequestDto {

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private Date year;

    @NotNull
    @NotEmpty
    private  Set<String> notes;

    @NotNull
    @NotEmpty
    private Set<Long> artists;

}
