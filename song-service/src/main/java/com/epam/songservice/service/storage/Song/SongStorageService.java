package com.epam.songservice.service.storage.Song;

import com.epam.resourceservice.model.Resource;
import com.epam.songservice.model.Song;

public interface SongStorageService {

    Song upload(org.springframework.core.io.Resource source, String name) throws Exception;

    Song upload(Resource resource) throws Exception;

    org.springframework.core.io.Resource download(Song entity) throws Exception;

    void delete(Song entity);

    boolean exist(Song entity);

    String test();
}
