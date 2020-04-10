package com.epam.jpa_repository;

import com.epam.model.Playlist;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends GenericRepository<Playlist, Long> {
}
