package com.epam.songservice.service.storage;

import com.epam.songservice.model.Song;

public interface SongStorageService {

    org.springframework.core.io.Resource download(Song entity) throws Exception;

//    org.springframework.core.io.Resource download(Long id);

    Song upload(org.springframework.core.io.Resource source, String name) throws Exception;

    void delete(Song entity);

//    void delete(Long id);

    boolean exist(Song entity);

//    boolean exist(Long id);

    String test();
}
