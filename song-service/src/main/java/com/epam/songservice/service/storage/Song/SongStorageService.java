package com.epam.songservice.service.storage.Song;

import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;

public interface SongStorageService {

    Song upload(Resource resource) throws Exception;

    Resource download(Song entity) throws Exception;

    void delete(Song entity) throws Exception;

    boolean exist(Song entity);

    String test();
}
