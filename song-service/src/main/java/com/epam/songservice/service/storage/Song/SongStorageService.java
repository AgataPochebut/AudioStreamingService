package com.epam.songservice.service.storage.Song;

import com.epam.songservice.model.Song;
import com.epam.storageservice.model.Resource;

public interface SongStorageService {

    Song upload1(Resource resource) throws Exception;

    Resource download1(Song entity) throws Exception;

    void delete(Song entity);

    String test();
}
