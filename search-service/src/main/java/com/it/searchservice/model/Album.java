package com.it.searchservice.model;

import com.it.commonservice.model.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class Album extends BaseEntity {

    @NotNull
    @NotEmpty
    private String name;

    private Integer year;

    private Set<String> notes;

    private Set<Artist> artists;

}
