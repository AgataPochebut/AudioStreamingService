package com.it.songservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UploadResultResponseDto {

    private Long id;

    private String status;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<UploadResultResponseDto> details;

}
