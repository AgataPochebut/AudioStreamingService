package com.it.playservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
@NoArgsConstructor
@Data
public class User {//extends BaseEntity {

    @Id
    private Long id;

//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private Set<Playlist> playlists;

}
