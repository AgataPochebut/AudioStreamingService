package com.epam.songservice.repository;

import com.epam.songservice.model.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends GenericRepository<Song, Long> {
}
