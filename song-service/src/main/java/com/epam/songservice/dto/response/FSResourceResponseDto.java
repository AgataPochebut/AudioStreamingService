package com.epam.songservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FSResourceResponseDto extends ResourceResponseDto {

    private String folderName;

}
