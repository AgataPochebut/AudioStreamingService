package com.epam.searchservice.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName="album")
public class FSResource extends Resource {

    private String path;

}
