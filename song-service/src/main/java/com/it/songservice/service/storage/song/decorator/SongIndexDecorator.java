package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.feign.index.SongIndexClient;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.song.SongStorageService;

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
            throw new Exception("Error when delete song from es");
        }

        super.delete(entity);
    }
}
