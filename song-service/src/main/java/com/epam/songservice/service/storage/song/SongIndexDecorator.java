package com.epam.songservice.service.storage.song;

import com.epam.songservice.feign.index.SongIndexClient;
import com.epam.songservice.model.Song;

public class SongIndexDecorator extends SongStorageDecorator {

    private SongIndexClient indexClient;

    public SongIndexDecorator(SongStorageService storageService, SongIndexClient indexService) {
        super(storageService);
        this.indexClient = indexService;
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Song entity = super.upload(source, name);

        try {
            indexClient.save(entity);
            return entity;
        }
        catch (Exception e){
            super.delete(entity);
            throw new Exception("Error when save song to es");
        }
    }

    @Override
    public void delete(Song entity) throws Exception {
        try {
            indexClient.delete(entity.getId());
        }
        catch (Exception e){
            throw new Exception("Error when delete song from db");
        }

        super.delete(entity);
    }
}
