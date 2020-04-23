package com.epam.songservice.repository;

import com.epam.songservice.model.Artist;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends GenericRepository<Artist, Long> {
}
