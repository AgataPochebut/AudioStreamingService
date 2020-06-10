package com.epam.songservice.repository;

import com.epam.songservice.model.Artist;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends GenericRepository<Artist, Long> {

    Optional<Artist> findByName(String s);

}
