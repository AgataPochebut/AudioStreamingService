package com.epam.songservice.service.storage.Song;

import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;

public class SongDBDecorator extends SongStorageDecorator {

    private SongRepositoryService repositoryService;

    public SongDBDecorator(SongStorageService storageService, SongRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Song upload(Resource resource) throws Exception {
        Song entity = super.upload(resource);
        entity = repositoryService.save(entity);
        return entity;
    }

    @Override
    public void delete(Song entity) {
        super.delete(entity);
        repositoryService.delete(entity);
    }

    @Override
    public String test() {
        return super.test() + " DB";
    }

}
