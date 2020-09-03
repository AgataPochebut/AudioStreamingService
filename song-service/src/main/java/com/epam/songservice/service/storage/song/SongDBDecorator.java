package com.epam.songservice.service.storage.song;

import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;

public class SongDBDecorator extends SongStorageDecorator {

    private SongRepositoryService repositoryService;

    public SongDBDecorator(SongStorageService storageService, SongRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Song entity = super.upload(source, name);

        try {
            entity = repositoryService.save(entity);
            return entity;
        } catch (Exception e) {
            super.delete(entity);
            throw new Exception("Error when save song to db");
        }
    }

    @Override
    public void delete(Song entity) throws Exception {
        try {
            repositoryService.deleteById(entity.getId());
        }
        catch (Exception e){
            throw new Exception("Error when delete song from db");
        }

        super.delete(entity);
    }
}
