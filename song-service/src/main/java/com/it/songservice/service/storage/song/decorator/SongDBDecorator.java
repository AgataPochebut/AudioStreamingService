package com.it.songservice.service.storage.song.decorator;

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

    @Override
    public boolean exist(Song entity) {
        if(repositoryService.existById(entity.getId())) return super.exist(entity);
        else return false;
    }

}
