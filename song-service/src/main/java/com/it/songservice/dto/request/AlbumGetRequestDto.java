package com.it.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class AlbumGetRequestDto {

    @Size(min = 5, max = 200)
    private String name;

    private Integer year;

    @Size(max = 200)
    private String artists;

    @Size(max = 200)
    private String genres;

    private  String notes;

}
