package com.epam.jpa_repository;

import com.epam.model.Artist;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends GenericRepository<Artist, Long> {
}
