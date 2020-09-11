package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.feign.conversion.ConversionClient;
import com.it.songservice.feign.index.SongIndexClient;
import com.it.songservice.service.repository.SongRepositoryService;
import com.it.songservice.service.storage.song.SongStorageService;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SongStorageDecoratorFactory {

    @Autowired
    private SongRepositoryService songRepositoryService;

    @Autowired
    private ConversionClient conversionClient;

    @Autowired
    private SongIndexClient songIndexClient;

    @Autowired
    private Mapper mapper;

    public SongStorageService create(SongStorageService service, Class<?> clazz) {
        if (SongConversionDecorator.class.equals(clazz)) {
            return new SongConversionDecorator(service, conversionClient);
        } else if (SongDBDecorator.class.equals(clazz)) {
            return new SongDBDecorator(service, songRepositoryService);
        } else if (SongIndexDecorator.class.equals(clazz)) {
            return new SongIndexDecorator(service, songIndexClient);
        } else if (SongMetadataDecorator.class.equals(clazz)) {
            return new SongMetadataDecorator(service, mapper);
        }
        return service;
    }

}
