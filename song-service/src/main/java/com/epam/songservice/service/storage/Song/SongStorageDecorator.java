package com.epam.songservice.service.storage.Song;

import com.epam.songservice.model.Song;
import com.epam.storageservice.model.Resource;

public abstract class SongStorageDecorator implements SongStorageService {

    protected SongStorageService storageService;

    public SongStorageDecorator(SongStorageService storageService){
        this.storageService = storageService;
    }

//    @Override
//    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
//        return storageService.upload(source, name);
//    }

    @Override
    public Song upload1(Resource resource) throws Exception {
        return storageService.upload1(resource);
    }

//    @Override
//    public org.springframework.core.io.Resource download(Song resource) throws Exception {
//        return storageService.download(resource);
//    }

        @Override
    public Resource download1(Song entity) throws Exception {
        return storageService.download1(entity);
    }

    @Override
    public void delete(Song entity) {
        storageService.delete(entity);
    }

//    @Override
//    public boolean exist(Song resource) {
//        return storageService.exist(resource);
//    }

    @Override
    public String test() {
        return storageService.test();
    }

}
