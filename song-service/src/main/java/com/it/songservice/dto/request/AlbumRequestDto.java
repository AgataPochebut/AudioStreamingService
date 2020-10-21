package com.it.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
public class AlbumRequestDto {

    @NotNull
    @NotEmpty
    private String name;

    private Integer year;

    private Set<Long> artists;

    private  Set<String> notes;

}
