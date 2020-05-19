package com.epam.playservice.repository;

import com.epam.playservice.model.Playlist;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends GenericRepository<Playlist, Long> {
}
