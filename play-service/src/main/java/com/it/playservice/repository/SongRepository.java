package com.it.playservice.repository;

import com.it.playservice.model.Song;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends GenericRepository<Song, Long> {

}
