package com.epam.songservice.service.storage.song;

import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;

import java.util.List;

public interface SongStorageService {

    Song upload(Resource resource) throws Exception;

    Resource download(Song entity) throws Exception;

    void delete(Song entity) throws Exception;

    boolean exist(Song entity);

    Song upload(org.springframework.core.io.Resource source, String name) throws Exception;
    List<Song> uploadZip(org.springframework.core.io.Resource source, String name) throws Exception;
    org.springframework.core.io.Resource download1(Song entity) throws Exception;

}