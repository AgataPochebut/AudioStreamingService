package com.epam.repository;

import com.epam.model.Genre;
import com.epam.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends GenericRepository<Genre, Long> {
}