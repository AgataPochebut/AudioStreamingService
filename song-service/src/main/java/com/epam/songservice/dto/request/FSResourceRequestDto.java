package com.epam.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FSResourceRequestDto extends ResourceRequestDto {

    private String path;

}
