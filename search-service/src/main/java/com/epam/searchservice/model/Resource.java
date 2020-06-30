package com.epam.searchservice.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Resource extends BaseEntity {

    @NotNull
    @NotEmpty
    private String name;

    private Long size;

    private String checksum;

}
