package com.epam.indexservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SongRequestDto {

    private Long id;

    private String title;
}
