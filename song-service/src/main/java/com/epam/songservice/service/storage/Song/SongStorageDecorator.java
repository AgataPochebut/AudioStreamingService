package com.epam.songservice.service.storage.Song;

import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;

public abstract class SongStorageDecorator implements SongStorageService {

    protected SongStorageService storageService;

    public SongStorageDecorator(SongStorageService storageService){
        this.storageService = storageService;
    }

    @Override
    public Song upload(Resource resource) throws Exception {
        return storageService.upload(resource);
    }

    @Override
    public Resource download(Song entity) throws Exception {
        return storageService.download(entity);
    }

    @Override
    public void delete(Song entity) throws Exception {
        storageService.delete(entity);
    }

    @Override
    public boolean exist(Song entity) {
        return storageService.exist(entity);
    }

    @Override
    public String test() {
        return storageService.test();
    }

}
