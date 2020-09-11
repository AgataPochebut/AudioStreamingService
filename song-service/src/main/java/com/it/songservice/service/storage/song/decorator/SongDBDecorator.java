package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.exception.UploadException;
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
            return repositoryService.save(entity);
        } catch (Exception e) {
            super.delete(entity);
        }

        throw new UploadException("DB");
    }

    @Override
    public void delete(Song entity) throws Exception {
        repositoryService.deleteById(entity.getId());

        super.delete(entity);
    }

    @Override
    public boolean exist(Song entity) throws Exception {
        if(repositoryService.existById(entity.getId())) return super.exist(entity);
        else return false;
    }

}
