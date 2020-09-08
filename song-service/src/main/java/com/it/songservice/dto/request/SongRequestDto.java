package com.it.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class SongRequestDto {

    @NotNull
    @NotEmpty
    private String title;

    private Date year;

    @NotNull
    @NotEmpty
    private Long resource;

    private Long album;

    private Set<String> notes;

}
