package com.it.searchservice.repository;

import com.it.searchservice.model.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends GenericRepository<Song, Long> {
}
