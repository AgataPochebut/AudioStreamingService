package com.epam.songservice.repository;

import com.epam.songservice.model.Song;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends GenericRepository<Song, Long> {

    Optional<Song> findByTitle(String s);

}
