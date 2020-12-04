package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.repository.SongRepositoryService;
import com.it.songservice.service.storage.song.SongStorageService;

public class SongDBDecorator extends SongStorageDecorator {

    private SongRepositoryService repositoryService;

    public SongDBDecorator(SongStorageService storageService, SongRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Song upload(Resource resource) throws Exception {
        Song entity = super.upload(resource);
        try {
            return repositoryService.save(entity);
        } catch (Exception e) {
            super.delete(entity);
            throw new UploadException("DB exc in "+ resource.getName(), e);
        }
    }

    @Override
    public void delete(Song entity) throws Exception {
        repositoryService.deleteById(entity.getId());
        super.delete(entity);
    }

}
