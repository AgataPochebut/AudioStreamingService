package com.it.songservice.service.repository;

import com.it.songservice.model.Artist;

public interface ArtistRepositoryService extends GenericRepositoryService<Artist, Long> {

    Artist findByName(String s);

}
