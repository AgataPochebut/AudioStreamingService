package com.epam.songservice.service.repository;

import com.epam.songservice.model.Genre;

public interface GenreRepositoryService extends GenericRepositoryService<Genre, Long> {

    Genre findByName(String s);

}
