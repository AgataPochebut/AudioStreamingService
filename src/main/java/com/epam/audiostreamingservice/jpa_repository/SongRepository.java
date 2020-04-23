package com.epam.audiostreamingservice.jpa_repository;

import com.epam.audiostreamingservice.model.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends GenericRepository<Song, Long> {
}
