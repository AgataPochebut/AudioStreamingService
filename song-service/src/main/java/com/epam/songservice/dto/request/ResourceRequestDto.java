package com.epam.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceRequestDto {

    private String name;

    private Long size;

    private String checksum;

}
