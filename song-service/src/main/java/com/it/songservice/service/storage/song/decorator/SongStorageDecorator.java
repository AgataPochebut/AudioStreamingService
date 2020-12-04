package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.song.SongStorageService;

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
    public void delete(Song entity) throws Exception {
        storageService.delete(entity);
    }

}
