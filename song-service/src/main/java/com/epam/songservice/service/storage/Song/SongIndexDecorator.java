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
        indexClient.create(entity);
        return entity;
    }

    @Override
    public void delete(Song entity) {
        super.delete(entity);
        indexClient.delete(entity.getId());
    }

    @Override
    public String test() {
        return super.test() + " Index";
    }

}
