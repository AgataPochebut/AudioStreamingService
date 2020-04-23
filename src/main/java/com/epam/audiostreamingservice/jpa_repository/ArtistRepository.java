package com.epam.audiostreamingservice.jpa_repository;

import com.epam.audiostreamingservice.model.Artist;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends GenericRepository<Artist, Long> {
}
