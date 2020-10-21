package com.it.songservice.service.repository;

import com.it.songservice.model.Song;

public interface SongRepositoryService extends GenericRepositoryService<Song, Long> {

    Song findByName(String s);

}
