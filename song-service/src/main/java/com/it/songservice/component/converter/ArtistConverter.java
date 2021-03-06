package com.it.songservice.component.converter;

import com.it.songservice.model.Artist;
import com.it.songservice.service.repository.ArtistRepositoryService;
import org.dozer.CustomConverter;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public
class ArtistConverter implements CustomConverter {

    private static ArtistRepositoryService repositoryService;
    private static Mapper mapper;

    @Autowired
    public ArtistConverter(ArtistRepositoryService repositoryService, Mapper mapper) {
        ArtistConverter.repositoryService = repositoryService;
        ArtistConverter.mapper = mapper;
    }

    /**
     * Instantiates a new Client converter.
     */
    public ArtistConverter() {
    }

    @Override
    public Object convert(Object dest, Object source, Class<?> destinationClass, Class<?> sourceClass) {
        if (source == null)
            return null;
        else if (source instanceof Set) {
            return ((Set) source).stream().map(s -> convert(dest, s, destinationClass, s.getClass())).collect(Collectors.toSet());
        } else if (source instanceof Map) {
            Map<String, Object> metadataMap = (Map<String, Object>) source;
            Artist entity = repositoryService.findByName((String) metadataMap.get("Name"));
            if (entity == null) {
                entity = mapper.map(metadataMap, Artist.class);
            }
            return entity;
        } else if (source instanceof Long) {
            return repositoryService.findById((Long) source);
        } else return null;
    }
}
