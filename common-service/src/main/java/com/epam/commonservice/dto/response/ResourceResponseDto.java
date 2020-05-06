package com.epam.commonservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResourceResponseDto {

    private Long id;

    private String name;

    private String path;

    private Long size;

    private String checksum;


}
