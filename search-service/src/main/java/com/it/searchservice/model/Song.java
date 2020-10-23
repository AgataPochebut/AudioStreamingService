package com.it.searchservice.model;

import com.it.commonservice.model.BaseEntity;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Document(indexName="songs")
public class Song extends BaseEntity {

    @NotNull
    @NotEmpty
    private String name;

    private Integer year;

    private Resource resource;

    private Set<String> notes;

    private Album album;

}
