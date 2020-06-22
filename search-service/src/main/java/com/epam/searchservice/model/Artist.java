package com.epam.searchservice.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Document(indexName="artist")
public class Artist extends BaseEntity {

    @NotNull
    @NotEmpty
    @Column(unique = true, nullable = false)
    private String name;

//    @NotNull
//    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "notes", joinColumns = @JoinColumn(name = "source_id"))
    private Set<String> notes;

//    @NotNull
//    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Artist_Genre",
            joinColumns = {@JoinColumn(name = "artist_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private Set<Genre> genres;

}
