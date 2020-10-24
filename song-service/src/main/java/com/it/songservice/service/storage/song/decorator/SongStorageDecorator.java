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
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        return storageService.upload(source, name);
    }

    @Override
    public void uploadZip(org.springframework.core.io.Resource source, String name) throws Exception {
        storageService.uploadZip(source, name);
    }

    @Override
    public org.springframework.core.io.Resource download(Song entity) throws Exception {
        return storageService.download(entity);
    }

    @Override
    public void delete(Song entity) throws Exception {
        storageService.delete(entity);
    }

    @Override
    public boolean exist(Song entity) throws Exception {
        return storageService.exist(entity);
    }




}
