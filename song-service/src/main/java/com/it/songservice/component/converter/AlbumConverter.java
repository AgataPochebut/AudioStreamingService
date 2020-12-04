package com.it.songservice.component.converter;

import com.it.songservice.model.Album;
import com.it.songservice.service.repository.AlbumRepositoryService;
import org.dozer.CustomConverter;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AlbumConverter implements CustomConverter {

    private static AlbumRepositoryService repositoryService;
    private static Mapper mapper;

    @Autowired
    public AlbumConverter(AlbumRepositoryService repositoryService, Mapper mapper) {
        AlbumConverter.repositoryService = repositoryService;
        AlbumConverter.mapper = mapper;
    }

    /**
     * Instantiates a new Client converter.
     */
    public AlbumConverter() {
    }

    @Override
    public Object convert(Object dest, Object source, Class<?> destinationClass, Class<?> sourceClass) {
        if (source == null)
            return null;
        else if (source instanceof Set) {
            return ((Set) source).stream().map(s -> convert(dest, s, destinationClass, s.getClass())).collect(Collectors.toSet());
        } else if (source instanceof Map) {
            Map<String, Object> metadataMap = (Map<String, Object>) source;
            // create new instead of find by name and artists
            Album fake = mapper.map(metadataMap, Album.class);
            List<?> list = repositoryService.findAll(fake);
            if(list.isEmpty()) return fake;
            else return list.get(0);
        } else if (source instanceof Long) {
            return repositoryService.findById((Long) source);
        } else if (source instanceof Album) {
            Album fake = (Album) source;
            List<?> list = repositoryService.findAll(fake);
            if (list.isEmpty()) return fake;
            else return list.get(0);
        } else return null;
    }
}
