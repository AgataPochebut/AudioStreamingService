package com.epam.songservice.configuration;

import com.epam.songservice.converter.AlbumConverter;
import com.epam.songservice.converter.ArtistConverter;
import com.epam.songservice.converter.GenreConverter;
import com.epam.songservice.model.Album;
import com.epam.songservice.model.Artist;
import com.epam.songservice.model.Genre;
import com.epam.songservice.model.Song;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MappingConfiguration {

    @Bean
    public Mapper mapper() {

        DozerBeanMapper mapper = new DozerBeanMapper();
//        mapper.setMappingFiles(Arrays.asList("dozer-config.xml"));
        mapper.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {

                mapping(Map.class, Song.class,
                        TypeMappingOptions.dateFormat("yyyy"))
                        .fields("Album", "Album", FieldsMappingOptions.customConverter(AlbumConverter.class))
                        .fields("Year", "Year")
                        .fields("Title", "Title");

                mapping(Map.class, Album.class,
                        TypeMappingOptions.dateFormat("yyyy"))
                        .fields("Artists", "Artists", FieldsMappingOptions.customConverter(ArtistConverter.class))
                        .fields("Year", "Year")
                        .fields("Title", "Title");

                mapping(Map.class, Artist.class)
                        .fields("Genres", "Genres", FieldsMappingOptions.customConverter(GenreConverter.class))
                        .fields("Name", "Name");

                mapping(Map.class, Genre.class)
                        .fields("Name", "Name");
            }
        });

        return mapper;
    }
}



