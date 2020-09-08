package com.it.songservice.repository;

import com.it.songservice.model.Album;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlbumRepository extends GenericRepository<Album, Long> {

    Optional<Album> findByTitle(String s);

}
