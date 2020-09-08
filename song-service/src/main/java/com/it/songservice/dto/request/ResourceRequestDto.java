package com.it.songservice.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ResourceRequestDto {

    @NotNull
    @NotEmpty
    private String name;

    private Long size;

    private String checksum;

}
