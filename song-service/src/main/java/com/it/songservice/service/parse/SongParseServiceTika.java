package com.it.songservice.service.parse;

import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;

import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SongParseServiceTika implements SongParseService {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private Mapper mapper;

    @Override
    public Song parse(Resource resource) throws Exception {
        org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);
        BufferedInputStream input = new BufferedInputStream(source.getInputStream());

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

}
