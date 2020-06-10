package com.epam.songservice.converter;

import com.epam.songservice.model.Album;
import com.epam.songservice.service.repository.AlbumService;
import lombok.SneakyThrows;
import org.dozer.CustomConverter;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AlbumConverter implements CustomConverter {

    private static AlbumService repositoryService;
    private static Mapper mapper;

    @Autowired
    public AlbumConverter(AlbumService repositoryService, Mapper mapper) {
        AlbumConverter.repositoryService = repositoryService;
        AlbumConverter.mapper = mapper;
    }

    /**
     * Instantiates a new Client converter.
     */
    public AlbumConverter() {
    }

    @SneakyThrows
    @Override
    public Object convert(Object dest, Object source, Class<?> destinationClass, Class<?> sourceClass) {

        if (source == null)
            return null;

        else if (source instanceof Map) {
            Map<String, Object> metadataMap = (Map<String, Object>) source;

            Album entity = repositoryService.findByTitle((String) metadataMap.get("Title"));

            if (entity == null) {
//                entity = Album.builder()
//                        .title((String) metadataMap.get("Title"))
//                        .artists(((Set<?>) metadataMap.get("Artists"))
//                                    .stream()
//                                    .map(a -> mapper.map((Map<String, Object>)a, Artist.class))
//                                    .collect(Collectors.toSet()))
//                    .build();
                entity = mapper.map(metadataMap, Album.class);
                repositoryService.save(entity);
            }
            else {
                entity = mapper.map(entity, Album.class);
            }

            return entity;
        } else if (source instanceof Long) {
            return repositoryService.findById((Long) source);
        } else return null;
    }
}
