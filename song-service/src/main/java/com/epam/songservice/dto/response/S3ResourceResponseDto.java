package com.epam.songservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class S3ResourceResponseDto extends ResourceResponseDto {

    private String bucketName;

}
