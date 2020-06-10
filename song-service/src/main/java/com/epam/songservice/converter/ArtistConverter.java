package com.epam.songservice.converter;

import com.epam.songservice.model.Artist;
import com.epam.songservice.service.repository.ArtistService;
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
class ArtistConverter implements CustomConverter {

    private static ArtistService repositoryService;
    private static Mapper mapper;

    @Autowired
    public ArtistConverter(ArtistService repositoryService, Mapper mapper) {
        ArtistConverter.repositoryService = repositoryService;
        ArtistConverter.mapper = mapper;
    }

    /**
     * Instantiates a new Client converter.
     */
    public ArtistConverter() {
    }

    @SneakyThrows
    @Override
    public Object convert(Object dest, Object source, Class<?> destinationClass, Class<?> sourceClass) {

        if (source == null)
            return null;

        else if(source instanceof Set) {
            return ((Set) source).stream().map(s -> {
                return convert(dest, s, destinationClass, s.getClass());
            }).collect(Collectors.toSet());
        }

        else if(source instanceof Map) {
            Map<String, Object> metadataMap = (Map<String, Object>) source;

            Artist entity = repositoryService.findByName((String)metadataMap.get("Name"));

            if(entity == null) {
//                entity = Artist.builder()
//                    .name((String) metadataMap.get("Name"))
//                    .genres(((Set<?>)metadataMap.get("Genres"))
//                                    .stream()
//                                    .map(g -> mapper.map((Map<String, Object>)g, Genre.class))
//                                    .collect(Collectors.toSet()))
//                    .build();
                entity = mapper.map(metadataMap, Artist.class);
                repositoryService.save(entity);
            } else {
                entity = mapper.map(entity, Artist.class);
            }

            return entity;
        }

        else if(source instanceof Long) {
            return repositoryService.findById((Long) source);
        }

        else return null;
    }
}
