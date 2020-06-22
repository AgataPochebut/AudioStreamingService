package com.epam.searchservice.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName="songs")
public class Song extends BaseEntity {

    private String title;

    private Date year;

//    private Set<String> notes;

//    @Field//(type = FieldType.Nested, includeInParent = false)
    private Album album;

}
