package com.epam.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class AlbumRequestDto {

    @NotNull
    @NotEmpty
    private String title;

    private Date year;

    private Set<Long> artists;

    private  Set<String> notes;

}
