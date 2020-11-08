package com.it.songservice.configuration;

import com.it.songservice.converter.AlbumConverter;
import com.it.songservice.converter.ArtistConverter;
import com.it.songservice.converter.GenreConverter;
import com.it.songservice.converter.LongConverter;
import com.it.songservice.dto.request.AlbumRequestDto;
import com.it.songservice.dto.request.ArtistRequestDto;
import com.it.songservice.dto.request.GenreRequestDto;
import com.it.songservice.dto.response.AlbumResponseDto;
import com.it.songservice.dto.response.ArtistResponseDto;
import com.it.songservice.dto.response.GenreResponseDto;
import com.it.songservice.dto.response.SongResponseDto;
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

    @Bean
    public Mapper mapper() {

        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {

                mapping(Map.class, Song.class)
                        .fields("Album", "Album", FieldsMappingOptions.customConverter(AlbumConverter.class))
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


                mapping(Song.class, SongResponseDto.class)
                        .fields("Album", "Album", FieldsMappingOptions.customConverter(LongConverter.class))
                        .fields("Resource", "Resource", FieldsMappingOptions.customConverter(LongConverter.class))
                        .fields("Name", "Name");

                mapping(Album.class, AlbumResponseDto.class, TypeMappingOptions.dateFormat("yyyy"))
                        .fields("Artists", "Artists", FieldsMappingOptions.customConverter(LongConverter.class))
                        .fields("Genres", "Genres", FieldsMappingOptions.customConverter(LongConverter.class))
                        .fields("Year", "Year")
                        .fields("Name", "Name");

                mapping(Artist.class, ArtistResponseDto.class)
                        .fields("Genres", "Genres", FieldsMappingOptions.customConverter(LongConverter.class))
                        .fields("Name", "Name");

                mapping(Genre.class, GenreResponseDto.class)
                        .fields("Name", "Name");

            }
        });

        return mapper;
    }


}



