package com.it.songservice.service.repository;

import com.it.songservice.model.Genre;

public interface GenreRepositoryService extends GenericRepositoryService<Genre, Long> {

    Genre findByName(String s);

}
