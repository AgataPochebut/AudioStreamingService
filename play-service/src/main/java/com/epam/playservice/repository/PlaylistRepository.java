package com.epam.playservice.repository;

import com.epam.playservice.model.Playlist;
import com.epam.playservice.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends GenericRepository<Playlist, Long> {

    @Query("SELECT t FROM Playlist t WHERE t.user = ?1")
    List<Playlist> findAll(User user);

    @Query("SELECT t FROM Playlist t WHERE t.user = ?1 and t.id = ?2")
    Optional<Playlist> findById(User user, Long id);

}
