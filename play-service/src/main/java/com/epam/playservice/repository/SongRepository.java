package com.epam.playservice.repository;

import com.epam.playservice.model.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends GenericRepository<Song, Long> {

}
