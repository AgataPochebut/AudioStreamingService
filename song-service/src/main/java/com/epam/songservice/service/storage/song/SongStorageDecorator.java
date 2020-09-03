package com.epam.songservice.service.storage.song;

import com.epam.songservice.model.Song;

import java.util.List;

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
    public List<Song> uploadZip(org.springframework.core.io.Resource source, String name) throws Exception {
        return storageService.uploadZip(source, name);
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
    public boolean exist(Song entity) {
        return storageService.exist(entity);
    }




}
