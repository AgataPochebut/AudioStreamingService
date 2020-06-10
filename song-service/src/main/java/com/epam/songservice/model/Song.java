package com.epam.songservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

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
    private String title;

//    @NotNull
//    @NotEmpty
//    @Column(nullable = false)
    private Date year;

//    @NotNull
//    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "notes", joinColumns = @JoinColumn(name = "source_id"))
    private Set<String> notes;

//    @NotNull
//    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

//    @NotNull
//    @NotEmpty
    @OneToOne
    @JoinColumn(name = "resource_id")
    private Resource resource;

}
