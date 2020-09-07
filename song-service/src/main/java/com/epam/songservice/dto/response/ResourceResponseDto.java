package com.epam.songservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceResponseDto {

    private Long id;

    private String name;

    private Long size;

    private String checksum;


}
