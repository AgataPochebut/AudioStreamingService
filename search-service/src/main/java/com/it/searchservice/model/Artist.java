package com.it.searchservice.model;

import com.it.commonservice.model.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
public class Artist extends BaseEntity {

    @NotNull
    @NotEmpty
    private String name;

    private Set<String> notes;

    private Set<Genre> genres;

}
