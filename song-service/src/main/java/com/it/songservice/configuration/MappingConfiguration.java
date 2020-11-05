package com.it.songservice.configuration;

import com.it.songservice.converter.AlbumConverter;
import com.it.songservice.converter.ArtistConverter;
import com.it.songservice.converter.GenreConverter;
import com.it.songservice.dto.request.AlbumRequestDto;
import com.it.songservice.dto.request.ArtistRequestDto;
import com.it.songservice.dto.request.GenreRequestDto;
import com.it.songservice.model.Album;
import com.it.songservice.model.Artist;
import com.it.songservice.model.Genre;
import com.it.songservice.model.Song;
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

//    private class CustomConverterImpl implements CustomConverter {
//        @Override
//        public Object convert(Object dest, Object source, Class<?> destinationClass, Class<?> sourceClass) {
//            if (source == null)
//                return null;
//            else if (source instanceof String) {
//                return Arrays.stream(((String) source).split(","))
//                        .map(s -> Long.parseLong(s))
//                        .collect(Collectors.toSet());
//            } else return null;
//        }
//    }

    @Bean
    public Mapper mapper() {

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {

                mapping(Map.class, Song.class)
                        .fields("Album", "Album", FieldsMappingOptions.customConverter(AlbumConverter.class))
//                        .fields("Albums", "Albums", FieldsMappingOptions.customConverter(AlbumConverter.class))
//                        .fields("Artists", "Artists", FieldsMappingOptions.customConverter(ArtistConverter.class))
                        .fields("Name", "Name");

                mapping(Map.class, Album.class, TypeMappingOptions.dateFormat("yyyy"))
                        .fields("Artists", "Artists", FieldsMappingOptions.customConverter(ArtistConverter.class))
                        .fields("Genres", "Genres", FieldsMappingOptions.customConverter(GenreConverter.class))
                        .fields("Year", "Year")
                        .fields("Name", "Name");

                mapping(Map.class, Artist.class)
                        .fields("Genres", "Genres", FieldsMappingOptions.customConverter(GenreConverter.class))
                        .fields("Name", "Name");

                mapping(Map.class, Genre.class)
                        .fields("Name", "Name");


                mapping(AlbumRequestDto.class, Album.class, TypeMappingOptions.dateFormat("yyyy"))
                        .fields("Artists", "Artists", FieldsMappingOptions.customConverter(ArtistConverter.class))
                        .fields("Genres", "Genres", FieldsMappingOptions.customConverter(GenreConverter.class))
                        .fields("Year", "Year")
                        .fields("Name", "Name");

                mapping(ArtistRequestDto.class, Artist.class)
                        .fields("Genres", "Genres", FieldsMappingOptions.customConverter(GenreConverter.class))
                        .fields("Name", "Name");

                mapping(GenreRequestDto.class, Genre.class)
                        .fields("Name", "Name");

            }
        });

        return mapper;
    }


}



