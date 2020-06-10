package com.epam.songservice.repository;

import com.epam.songservice.model.Genre;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends GenericRepository<Genre, Long> {

    Optional<Genre> findByName(String s);

}
