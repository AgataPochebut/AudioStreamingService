package com.epam.commonservice.model;

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
public class Album extends BaseEntity {

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

//    @NotNull
//    @NotEmpty
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "Album_Artist",
//            joinColumns = {@JoinColumn(name = "album_id")},
//            inverseJoinColumns = {@JoinColumn(name = "artist_id")})
//    private Set<Artist> artists;

}
