package com.epam.searchservice.repository;

import com.epam.searchservice.model.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends GenericRepository<Song, Long> {
}
