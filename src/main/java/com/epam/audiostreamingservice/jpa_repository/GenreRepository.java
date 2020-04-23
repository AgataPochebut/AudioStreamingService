package com.epam.audiostreamingservice.jpa_repository;

import com.epam.audiostreamingservice.model.Genre;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends GenericRepository<Genre, Long> {
}
