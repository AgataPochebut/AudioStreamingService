package com.epam.songservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="SONGS")
@NoArgsConstructor
@Data
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String title;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private Date year;

    @NotNull
    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "notes", joinColumns = @JoinColumn(name = "source_id"))
    private Set<String> notes;

    @NotNull
    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private Album album;

    @NotNull
    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "storage_id", nullable = false)
    private Storage storage;
}
