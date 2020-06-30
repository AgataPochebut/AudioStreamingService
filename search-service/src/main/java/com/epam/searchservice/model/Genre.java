package com.epam.searchservice.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class Genre extends BaseEntity {

    @NotNull
    @NotEmpty
    String name;

}
