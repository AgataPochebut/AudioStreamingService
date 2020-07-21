package com.epam.playservice.model;

import com.epam.commonservice.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PLAYLISTS")
public class Playlist extends BaseEntity {

    @NotNull
    @NotEmpty
    private String title;

    @ManyToMany
    @JoinTable(name = "Playlist_Song",
            joinColumns = {@JoinColumn(name = "playlist_id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id")})
    private Set<Long> songs;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
