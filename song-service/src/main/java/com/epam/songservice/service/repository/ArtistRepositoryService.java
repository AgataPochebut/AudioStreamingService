package com.epam.songservice.service.repository;

import com.epam.songservice.model.Artist;

public interface ArtistRepositoryService extends GenericRepositoryService<Artist, Long> {

    Artist findByName(String s);

}
