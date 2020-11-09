package com.it.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SongGetRequestDto {

    private String name;

    private Long resource;

    private Long album;

    private String notes;

}
