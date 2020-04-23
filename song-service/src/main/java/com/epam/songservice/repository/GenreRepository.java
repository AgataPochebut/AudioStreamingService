package com.epam.songservice.repository;

import com.epam.songservice.model.Genre;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends GenericRepository<Genre, Long> {
}
