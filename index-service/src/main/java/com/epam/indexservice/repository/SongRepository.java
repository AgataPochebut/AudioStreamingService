package com.epam.indexservice.repository;

import com.epam.indexservice.model.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends GenericRepository<Song, Long> {
}
