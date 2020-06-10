package com.epam.songservice.service.storage.Song;

import com.epam.songservice.model.Song;

public abstract class SongStorageDecorator implements SongStorageService {

    protected SongStorageService storageService;

    public SongStorageDecorator(SongStorageService storageService){
        this.storageService = storageService;
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        return storageService.upload(source, name);
    }

    @Override
    public org.springframework.core.io.Resource download(Song resource) throws Exception {
        return storageService.download(resource);
    }

    @Override
    public void delete(Song resource) {
        storageService.delete(resource);
    }

    @Override
    public boolean exist(Song resource) {
        return storageService.exist(resource);
    }

    @Override
    public String test() {
        return storageService.test();
    }

}
