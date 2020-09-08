package com.it.playservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class PlaylistRequestDto {

    @NotNull
    @NotEmpty
    private String title;

}
