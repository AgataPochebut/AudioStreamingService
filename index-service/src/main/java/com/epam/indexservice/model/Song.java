package com.epam.indexservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "service", type = "songs")
public class Song extends BaseEntity {

//    @NotNull
//    @NotEmpty
    private String title;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Album album;
}
