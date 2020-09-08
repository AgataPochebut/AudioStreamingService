package com.it.playservice.repository;

import com.it.playservice.model.Playlist;
import com.it.playservice.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends GenericRepository<Playlist, Long> {

    @Query("SELECT t FROM Playlist t WHERE t.user = ?1")
    List<Playlist> findAllByUser(User user);

    @Query("SELECT t FROM Playlist t WHERE t.id = ?1 and t.user = ?2")
    Optional<Playlist> findByIdAndByUser(Long id, User user);

//    @Query("SELECT t FROM Playlist_Song t WHERE t.song_id = ?1")
//    Optional<Playlist> findBySong(Long song_id);

}
