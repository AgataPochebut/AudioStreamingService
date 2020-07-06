package com.epam.songservice.service.storage.Song;

import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;
import com.epam.storageservice.model.Resource;

public class SongDBDecorator extends SongStorageDecorator {

    private SongRepositoryService repositoryService;

    public SongDBDecorator(SongStorageService storageService, SongRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Song upload1(Resource resource) throws Exception {
        Song entity = super.upload1(resource);
        return repositoryService.save(entity);
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
