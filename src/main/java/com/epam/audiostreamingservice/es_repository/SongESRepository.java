package com.epam.audiostreamingservice.es_repository;

import com.epam.audiostreamingservice.model.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongESRepository extends GenericESRepository<Song, Long> {
}
