package com.it.songservice.service.repository;

import com.it.songservice.dto.request.GenreGetRequestDto;
import com.it.songservice.model.Genre;

import java.util.List;

public interface GenreRepositoryService extends GenericRepositoryService<Genre, Long> {

    Genre findByName(String s);

    List<Genre> findAll(GenreGetRequestDto requestDto);

}
