package com.epam.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name="ALBUMS")
@Document(indexName = "techfou", type = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    private String title;

    @NotNull
    @NotEmpty
    private Date year;

    @NotNull
    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "notes", joinColumns = @JoinColumn(name = "source_id"))
    private Set<String> notes;

    @NotNull
    @NotEmpty
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Album_Artist",
            joinColumns = {@JoinColumn(name = "album_id")},
            inverseJoinColumns = {@JoinColumn(name = "artist_id")})
    private Set<Artist> artists;

}
