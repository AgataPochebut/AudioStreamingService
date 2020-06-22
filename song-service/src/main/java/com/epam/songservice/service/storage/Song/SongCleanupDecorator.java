package com.epam.songservice.service.storage.Song;//package com.epam.service.storage;

import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.ResourceRepositoryService;

public class SongCleanupDecorator extends SongStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public SongCleanupDecorator(SongStorageService service, ResourceRepositoryService repositoryService){
        super(service);
        this.repositoryService = repositoryService;
    }

    @Override
    public Song upload(final org.springframework.core.io.Resource source, String name) throws Exception {
        return super.upload(source, name);
    }
}
