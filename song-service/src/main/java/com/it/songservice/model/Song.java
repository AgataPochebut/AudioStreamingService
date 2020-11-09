package com.it.songservice.model;

import com.it.commonservice.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="SONGS")
public class Song extends BaseEntity {

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String name;

    @NotNull
    @OneToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "album_id")
    private Album album;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name = "Song_Album",
//            joinColumns = {@JoinColumn(name = "song_id")},
//            inverseJoinColumns = {@JoinColumn(name = "album_id")})
//    private Set<Album> albums;
//
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name = "Song_Artist",
//            joinColumns = {@JoinColumn(name = "song_id")},
//            inverseJoinColumns = {@JoinColumn(name = "artist_id")})
//    private Set<Artist> artists;

//    @ElementCollection
//    @CollectionTable(name = "notes", joinColumns = @JoinColumn(name = "source_id"))
//    @Column(name = "note")
//    private Set<String> notes;

}
