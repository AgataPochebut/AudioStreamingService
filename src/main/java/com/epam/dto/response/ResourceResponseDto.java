package com.epam.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class ResourceResponseDto {

    private Long id;

    private String name;

    private String path;

    private Long size;

    private String checksum;


}
