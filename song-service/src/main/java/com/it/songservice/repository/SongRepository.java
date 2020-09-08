package com.it.songservice.repository;

import com.it.songservice.model.Song;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends GenericRepository<Song, Long> {

    Optional<Song> findByTitle(String s);

}
