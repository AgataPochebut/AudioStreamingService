package com.epam.songservice.converter;

import com.epam.songservice.model.Genre;
import com.epam.songservice.service.repository.GenreRepositoryService;
import lombok.SneakyThrows;
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
class GenreConverter implements CustomConverter {

    private static GenreRepositoryService repositoryService;
    private static Mapper mapper;

    @Autowired
    public GenreConverter(GenreRepositoryService repositoryService, Mapper mapper) {
        GenreConverter.repositoryService = repositoryService;
        GenreConverter.mapper = mapper;
    }

    /**
     * Instantiates a new Client converter.
     */
    public GenreConverter() {
    }

    @SneakyThrows
    @Override
    public Object convert(Object dest, Object source, Class<?> destinationClass, Class<?> sourceClass) {

        if (source == null)
            return null;

        else if(source instanceof Set) {
            return ((Set) source).stream().map(s -> convert(dest, s, destinationClass, s.getClass())).collect(Collectors.toSet());
        }

        else if(source instanceof Map) {
            Map<String, Object> metadataMap = (Map<String, Object>) source;

            Genre entity = repositoryService.findByName((String)metadataMap.get("Name"));

            if(entity == null) {
                entity = mapper.map(metadataMap, Genre.class);
                repositoryService.save(entity);
            }
            else {
                entity = mapper.map(entity, Genre.class);
            }

            return entity;
        }

        else if(source instanceof Long) {
            return repositoryService.findById((Long) source);
        }

        else return null;
    }
}
