package com.epam.songservice.service.storage.Song;

import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.SongRepositoryService;

public class SongDedupingDecorator extends SongStorageDecorator {

    private SongRepositoryService repositoryService;

    public SongDedupingDecorator(SongStorageService storageService, SongRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        if(repositoryService.findByTitle(name) != null) throw new Exception("Exist");
        else return super.upload(source, name);
    }

    @Override
    public String test() {
        return super.test() + " Deduping";
    }

}
