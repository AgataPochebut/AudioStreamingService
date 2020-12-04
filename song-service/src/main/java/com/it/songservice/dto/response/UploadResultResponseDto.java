package com.it.songservice.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UploadResultResponseDto {

    private Long id;

    private String status;

    private Set<String> messages;

    private Set<UploadResultResponseDto> details;

}
