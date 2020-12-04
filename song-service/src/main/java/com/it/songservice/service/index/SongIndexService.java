package com.it.songservice.service.index;

import com.it.songservice.model.Song;

public interface SongIndexService {

   void save(Song entity);

   void delete(Song entity);
}
