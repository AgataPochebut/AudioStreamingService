package com.epam.dto.request;

import com.epam.model.Album;
import com.epam.model.Storage;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@Data
public class SongRequestDto {

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private Date year;

    @NotNull
    @NotEmpty
    private Set<String> notes;

    @NotNull
    @NotEmpty
    private Long album;

    @NotNull
    @NotEmpty
    private Long storage;

}
