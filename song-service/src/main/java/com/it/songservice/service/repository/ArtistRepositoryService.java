package com.it.songservice.service.repository;

import com.it.songservice.dto.request.ArtistGetRequestDto;
import com.it.songservice.model.Artist;

import java.util.List;

public interface ArtistRepositoryService extends GenericRepositoryService<Artist, Long> {

    Artist findByName(String s);

    List<Artist> findAll(ArtistGetRequestDto requestDto);

}
