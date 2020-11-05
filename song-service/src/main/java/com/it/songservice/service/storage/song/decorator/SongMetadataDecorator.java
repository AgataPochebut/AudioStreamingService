package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.storage.song.SongStorageService;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.dozer.Mapper;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SongMetadataDecorator extends SongStorageDecorator {

    private ResourceStorageServiceManager resourceStorageServiceManager;

    private Mapper mapper;

    public SongMetadataDecorator(SongStorageService storageService,
                                 ResourceStorageServiceManager resourceStorageServiceManager,
                                 Mapper mapper) {
        super(storageService);
        this.resourceStorageServiceManager = resourceStorageServiceManager;
        this.mapper = mapper;
    }

    @Override
    public Song upload(Resource resource) throws Exception {
        Song entity = super.upload(resource);
        try {
            org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);
            Song entity1 = parse(source);
            entity.setAlbum(entity1.getAlbum());
            entity.setName(entity1.getName());
//            entity.setNotes(entity1.getNotes());
            return entity;
        }
        catch (Exception e) {
            super.delete(entity);
            throw new UploadException("Metadata exc in "+ resource.getName(), e);
        }
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Song entity = super.upload(source, name);
        try {
            Song entity1 = parse(source);
            entity.setAlbum(entity1.getAlbum());
            entity.setName(entity1.getName());
//            entity.setNotes(entity1.getNotes());
            return entity;
        }
        catch (Exception e) {
            super.delete(entity);
            throw new UploadException("Metadata exc in "+ name, e);
        }
    }

    public Song parse(org.springframework.core.io.Resource source) throws IOException, TikaException, SAXException, ParseException {
        InputStream input = source.getInputStream();
        ContentHandler handler = new BodyContentHandler();

        Parser parser = new Mp3Parser();
        Metadata metadata = new Metadata();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);

        Integer year = 0;
        if (metadata.get("xmpDM:releaseDate") != null
                && !metadata.get("xmpDM:releaseDate").isEmpty()) {
            year = Integer.parseInt(metadata.get("xmpDM:releaseDate").substring(0,4));
        }

        Set genres = null;
        if (metadata.get("xmpDM:genre") != null
                && !metadata.get("xmpDM:genre").isEmpty()) {
            genres = Arrays.stream(metadata.get("xmpDM:genre").split(", "))
                    .map(g -> {
                        Map<String, Object> genreMap = new HashMap<>();
                        genreMap.put("Name", g);
                        return genreMap;
                    })
                    .collect(Collectors.toSet());
        }

        Set artists = null;
        if (metadata.get("xmpDM:artist") != null
                && !metadata.get("xmpDM:artist").isEmpty()) {
            Set finalGenres = genres;
            artists = Arrays.stream(metadata.get("xmpDM:artist").split(", "))
                .map(a -> {
                    Map<String, Object> artistMap = new HashMap<>();
                    artistMap.put("Name", a);
                    artistMap.put("Genres", finalGenres);
                    return artistMap;
                }).collect(Collectors.toSet());
        }

        String title = "Без названия";
        if (metadata.get("title") != null
                && !metadata.get("title").isEmpty()) {
            title = metadata.get("title");
        }

        Map album = new HashMap<>();
        if (metadata.get("xmpDM:album") != null
                && !metadata.get("xmpDM:album").isEmpty()) {
            album.put("Name", metadata.get("xmpDM:album"));
        } else {
            album.put("Name", title);
        }
        album.put("Year", year);
        album.put("Artists", artists);

        Map song = new HashMap<String, Object>();
        song.put("Name", title);
        song.put("Album", album);

        return mapper.map(song, Song.class);
    }

}
