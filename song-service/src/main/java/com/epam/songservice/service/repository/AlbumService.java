package com.epam.songservice.service.repository;

import com.epam.songservice.model.Album;

public interface AlbumService extends GenericService<Album, Long> {

    Album findByTitle(String s);

}
