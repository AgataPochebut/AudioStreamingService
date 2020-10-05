package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.exception.UploadException;
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

        Throwable lastException;
        try {
            indexClient.save(entity);
            return entity;
        }
        catch (Exception e){
            super.delete(entity);
            lastException = e;
        }
        throw new UploadException("ES exc in "+ name, lastException);
    }

    @Override
    public void delete(Song entity) throws Exception {
        indexClient.delete(entity.getId());
        super.delete(entity);
    }
}
