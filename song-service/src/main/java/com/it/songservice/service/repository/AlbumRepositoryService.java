package com.it.songservice.service.repository;

import com.it.songservice.dto.request.AlbumGetRequestDto;
import com.it.songservice.model.Album;

import java.util.List;

public interface AlbumRepositoryService extends GenericRepositoryService<Album, Long> {

    Album findByName(String s);

    List<Album> findAll(AlbumGetRequestDto requestDto);

    List<Album> findAll(Album requestDto);

}
