package com.epam.audiostreamingservice.jpa_repository;

import com.epam.audiostreamingservice.model.Playlist;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistRepository extends GenericRepository<Playlist, Long> {
}
