package com.it.songservice.service.index;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.it.songservice.model.Song;

public interface SongIndexService {

   void save(Song entity) throws JsonProcessingException;

   void delete(Song entity);
}
