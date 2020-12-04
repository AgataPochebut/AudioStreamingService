package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.index.SongIndexService;
import com.it.songservice.service.storage.song.SongStorageService;

public class SongIndexDecorator extends SongStorageDecorator {

    private SongIndexService indexService;

    public SongIndexDecorator(SongStorageService storageService, SongIndexService indexService) {
        super(storageService);
        this.indexService = indexService;
    }


    @Override
    public Song upload(Resource resource) throws Exception {
        Song entity = super.upload(resource);
        try {
            indexService.save(entity);
        } catch (Exception e) {
            super.delete(entity);
            throw new UploadException("ES exc in "+ resource.getName(), e);
        }
        return entity;
    }

    @Override
    public void delete(Song entity) throws Exception {
        try {
            indexService.delete(entity);
        } catch (Exception e) {

        }
        super.delete(entity);
    }
}
