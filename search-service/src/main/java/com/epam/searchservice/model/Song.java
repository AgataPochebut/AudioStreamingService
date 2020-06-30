package com.epam.searchservice.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@Document(indexName="songs")
public class Song extends BaseEntity {

    @NotNull
    @NotEmpty
    private String title;

    private Date year;

    private Resource resource;

    private Set<String> notes;

    private Album album;

}
