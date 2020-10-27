package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.exception.UploadException;
import com.it.songservice.feign.index.SongIndexClient;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.song.SongStorageService;

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
            indexClient.save(entity);
        } catch (Exception e) {
            super.delete(entity);
            throw new UploadException("ES exc in "+ resource.getName(), e);
        }
        return entity;
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Song entity = super.upload(source, name);
        try {
            indexClient.save(entity);
        } catch (Exception e) {
//            super.delete(entity);
//            throw new UploadException("ES exc in "+ name, e);
        }
        return entity;
    }

    @Override
    public void delete(Song entity) throws Exception {
        try {
            indexClient.delete(entity.getId());
        } catch (Exception e) {

        }
        super.delete(entity);
    }
}
