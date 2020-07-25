package com.epam.songservice.service.repository;

import com.epam.songservice.model.Album;

public interface AlbumRepositoryService extends GenericRepositoryService<Album, Long> {

    Album findByTitle(String s);

}
