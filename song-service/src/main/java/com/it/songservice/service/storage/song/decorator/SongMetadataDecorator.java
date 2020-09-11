package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.song.SongStorageService;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.dozer.Mapper;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SongMetadataDecorator extends SongStorageDecorator {

    private Mapper mapper;

    public SongMetadataDecorator(SongStorageService storageService, Mapper mapper) {
        super(storageService);
        this.mapper = mapper;
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Song entity = super.upload(source, name);

        try {
            InputStream input = source.getInputStream();
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);

            Map<String, Object> metadataMap = new HashMap<>();
            if (metadata.get("title") != null && !metadata.get("title").isEmpty()) {
                metadataMap.put("Title", metadata.get("title"));
            } else {
                metadataMap.put("Title", "Без названия");
            }
            metadataMap.put("Year", metadata.get("xmpDM:releaseDate"));

            if (metadata.get("xmpDM:album") != null && !metadata.get("xmpDM:album").isEmpty()) {
                Map<String, Object> albumMap = new HashMap<>();
                albumMap.put("Title", metadata.get("xmpDM:album"));
                albumMap.put("Year", metadata.get("xmpDM:releaseDate"));

                if (metadata.get("xmpDM:artist") != null && !metadata.get("xmpDM:artist").isEmpty()) {
                    albumMap.put("Artists", Arrays.stream(metadata.get("xmpDM:artist").split(", "))
                            .map(a -> {
                                Map<String, Object> artistMap = new HashMap<>();
                                artistMap.put("Name", a);

                                if (metadata.get("xmpDM:genre") != null && !metadata.get("xmpDM:genre").isEmpty()) {
                                    artistMap.put("Genres", Arrays.stream(metadata.get("xmpDM:genre").split(", "))
                                            .map(g -> {
                                                Map<String, Object> genreMap = new HashMap<>();
                                                genreMap.put("Name", g);
                                                return genreMap;
                                            })
                                            .collect(Collectors.toSet()));
                                }

                                return artistMap;
                            })
                            .collect(Collectors.toSet()));
                }

                metadataMap.put("Album", albumMap);
            }

            Song entity1 = mapper.map(metadataMap, Song.class);
            entity.setAlbum(entity1.getAlbum());
            entity.setTitle(entity1.getTitle());
            entity.setYear(entity1.getYear());
            entity.setNotes(entity1.getNotes());
            return entity;
        }
        catch (Exception e) {
            super.delete(entity);
        }

        throw new UploadException("Metadata");
    }

}
