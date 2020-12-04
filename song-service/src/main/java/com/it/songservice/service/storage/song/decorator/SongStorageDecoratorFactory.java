package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.service.index.SongIndexService;
import com.it.songservice.service.parse.SongParseService;
import com.it.songservice.service.repository.SongRepositoryService;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.storage.song.SongStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SongStorageDecoratorFactory {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private SongRepositoryService songRepositoryService;

    @Autowired
    @Qualifier("songIndexServiceAsync")
    private SongIndexService songIndexService;

    @Autowired
    private SongParseService songParseService;

    public SongStorageService create(SongStorageService service, Class<?> clazz) {
        if (SongDBDecorator.class.equals(clazz)) {
            return new SongDBDecorator(service, songRepositoryService);
        } else if (SongIndexDecorator.class.equals(clazz)) {
            return new SongIndexDecorator(service, songIndexService);
        } else if (SongMetadataDecorator.class.equals(clazz)) {
            return new SongMetadataDecorator(service, songParseService);
        }
        return service;
    }

}
