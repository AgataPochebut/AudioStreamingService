package com.it.songservice.service.storage.song.decorator;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.storage.song.SongStorageService;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import de.odysseus.ithaka.audioinfo.AudioInfo;
import de.odysseus.ithaka.audioinfo.mp3.ID3v2Exception;
import de.odysseus.ithaka.audioinfo.mp3.ID3v2Info;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.dozer.Mapper;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
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
            BufferedInputStream inputStream = new BufferedInputStream(source.getInputStream());

            Song entity1 = parse(inputStream);
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
            BufferedInputStream inputStream = new BufferedInputStream(source.getInputStream());

            Song entity1 = parse(inputStream);
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

    public Song parse_(InputStream input) throws IOException, TikaException, SAXException, ParseException, InvalidDataException, UnsupportedTagException, ID3v2Exception {
        ContentHandler handler = new BodyContentHandler();
        Parser parser = new Mp3Parser();
        Metadata metadata = new Metadata();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);

        Set genres = null;
        if (metadata.get("xmpDM:genre") != null
                && !metadata.get("xmpDM:genre").isEmpty()) {
            genres = Arrays.stream(metadata.get("xmpDM:genre").split(", "))
                    .map(g -> {
                        Map genreMap = new HashMap<>();
                        genreMap.put("Name", g);
                        return genreMap;
                    })
                    .collect(Collectors.toSet());
        }

        Set artists = null;
        if (metadata.get("xmpDM:artist") != null
                && !metadata.get("xmpDM:artist").isEmpty()) {
            artists = Arrays.stream(metadata.get("xmpDM:artist").split(", "))
                    .map(a -> {
                        Map artistMap = new HashMap<>();
                        artistMap.put("Name", a);
                        return artistMap;
                    }).collect(Collectors.toSet());
        }

        Integer year = 0;
        if (metadata.get("xmpDM:releaseDate") != null
                && !metadata.get("xmpDM:releaseDate").isEmpty()) {
            year = Integer.parseInt(metadata.get("xmpDM:releaseDate").substring(0,4));
        }

        String title = "Без названия";
        if (metadata.get("title") != null
                && !metadata.get("title").isEmpty()) {
            title = metadata.get("title");
        }

        Map albumMap = new HashMap<>();
        if (metadata.get("xmpDM:album") != null
                && !metadata.get("xmpDM:album").isEmpty()) {
            albumMap.put("Name", metadata.get("xmpDM:album"));
        } else {
            albumMap.put("Name", title);
        }
        albumMap.put("Year", year);
        albumMap.put("Artists", artists);
        albumMap.put("Genres", genres);

        Map songMap = new HashMap<>();
        songMap.put("Name", title);
        songMap.put("Album", albumMap);

        return mapper.map(songMap, Song.class);
    }

    public Song parse(InputStream input) throws IOException, TikaException, SAXException, ParseException, InvalidDataException, UnsupportedTagException, ID3v2Exception {
        AudioInfo audioInfo = new ID3v2Info(input);

        String genresName = audioInfo.getGenre();
        Set genres = null;
        if (genresName != null && !genresName.isEmpty()) {
            genres = Arrays.stream(genresName.split(", "))
                    .map(g -> {
                        Map genreMap = new HashMap<>();
                        genreMap.put("Name", g);
                        return genreMap;
                    })
                    .collect(Collectors.toSet());
        }

        String artistsName = audioInfo.getArtist();
        Set artists = null;
        if (artistsName != null && !artistsName.isEmpty()) {
            artists = Arrays.stream(artistsName.split(", "))
                    .map(a -> {
                        Map artistMap = new HashMap<>();
                        artistMap.put("Name", a);
                        return artistMap;
                    }).collect(Collectors.toSet());
        }

        Integer year = Integer.valueOf(audioInfo.getYear());

        String albumName = audioInfo.getAlbum();
        String songName = audioInfo.getTitle();

        Map albumMap = new HashMap<>();
        if (albumName != null && !albumName.isEmpty()) {
            albumMap.put("Name", albumName);
        } else if (songName != null && !songName.isEmpty()) {
            albumMap.put("Name", songName);
        } else albumMap.put("Name", "Без названия");
        albumMap.put("Year", year);
        albumMap.put("Artists", artists);
        albumMap.put("Genres", genres);

        Map songMap = new HashMap<>();
        if (songName != null && !songName.isEmpty()) {
            songMap.put("Name", songName);
        } else songMap.put("Name", "Без названия");
        songMap.put("Album", albumMap);

        return mapper.map(songMap, Song.class);
    }

}
