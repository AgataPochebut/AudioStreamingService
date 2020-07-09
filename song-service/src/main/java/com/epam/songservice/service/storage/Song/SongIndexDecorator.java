package com.epam.songservice.service.storage.Song;

import com.epam.songservice.feign.index.SongIndexClient;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;

public class SongIndexDecorator extends SongStorageDecorator {

    private SongIndexClient indexClient;

    public SongIndexDecorator(SongStorageService storageService, SongIndexClient indexService) {
        super(storageService);
        this.indexClient = indexService;
    }

    @Override
    public Song upload(Resource resource) throws Exception {
        Song entity = super.upload(resource);

        try {
            indexClient.create(entity);
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

    @Override
    public String test() {
        return super.test() + " Index";
    }

}
