package com.epam.searchservice.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName="album")
public class Album extends BaseEntity {

    private String title;

    private Date year;

//    private Set<String> notes;

//    private Set<Artist> artists;

}
