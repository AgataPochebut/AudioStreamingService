package com.epam.searchservice.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
public class Album extends BaseEntity {

    @NotNull
    @NotEmpty
    private String title;

    private Date year;

    private Set<String> notes;

    private Set<Artist> artists;

}
