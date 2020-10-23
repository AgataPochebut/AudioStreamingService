package com.it.songservice.service.repository;

import com.it.songservice.model.Album;

public interface AlbumRepositoryService extends GenericRepositoryService<Album, Long> {

    Album findByName(String s);

}
