package com.epam.songservice.service.storage;

import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SongStorageServiceImpl implements StorageService<Song, Long> {

    @Autowired
    private ResourceStorageService storageService;

    @Override
    public org.springframework.core.io.Resource download(Song entity) throws Exception {
        Resource resource = entity.getResource();
        return storageService.download(resource);
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = storageService.upload(source, name);
        return Song.builder()
                .resource(resource)
                .title("test")
                .build();
    }

    @Override
    public void delete(Song entity) {
        Resource resource = entity.getResource();
        storageService.delete(resource);
    }

    @Override
    public boolean exist(Song entity) {
        Resource resource = entity.getResource();
        return storageService.exist(resource);
    }

    @Override
    public String test() {
        return "Song";
    }

}
