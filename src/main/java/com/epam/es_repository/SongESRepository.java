package com.epam.es_repository;

import com.epam.model.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongESRepository extends GenericESRepository<Song, Long> {
}
