package com.epam.indexservice.model;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Document(indexName = "service", type = "albums")
public class Album extends BaseEntity {

    @NotNull
    @NotEmpty
    private String title;
}
