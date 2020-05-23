package com.epam.playservice.repository;

import com.epam.playservice.model.Playlist;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends GenericRepository<Playlist, Long> {

    @Query("SELECT t FROM Playlist t WHERE t.user = ?1")
    List<Playlist> findAll(Long user_id);

    @Query("SELECT t FROM Playlist t WHERE t.user = ?1 and t.id = ?2")
    Optional<Playlist> findById(Long user_id, Long id);

}
