package com.epam.jpa_repository;

import com.epam.model.Genre;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends GenericRepository<Genre, Long> {
}
