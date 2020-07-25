package com.epam.playservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SONGS")
@NoArgsConstructor
@Data
public class Song {//extends BaseEntity {

    @Id
    private Long id;

//    @ManyToMany(mappedBy = "songs", fetch = FetchType.EAGER)
//    private Set<Playlist> playlists;

}
