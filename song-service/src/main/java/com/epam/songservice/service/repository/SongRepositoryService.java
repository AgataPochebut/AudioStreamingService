package com.epam.songservice.service.repository;

import com.epam.songservice.model.Song;

public interface SongRepositoryService extends GenericService<Song, Long>  {

    Song findByTitle(String s);

}
