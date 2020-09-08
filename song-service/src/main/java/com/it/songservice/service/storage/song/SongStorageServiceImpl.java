package com.it.songservice.service.storage.song;

import com.it.songservice.jms.Producer;
import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SongStorageServiceImpl implements SongStorageService {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private Mapper mapper;

    @Autowired
    private Producer producer;

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
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

        Song entity = mapper.map(metadataMap, Song.class);
        Resource resource = resourceStorageServiceManager.upload(source, name);
        entity.setResource(resource);
        return entity;
    }

    @Override
    public List<Song> uploadZip(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = resourceStorageServiceManager.upload(source, name);
        List<Song> list = producer.upload(resource);
        resourceStorageServiceManager.delete(resource);
        return list;
    }

    @Override
    public org.springframework.core.io.Resource download(Song entity) throws Exception {
        Resource resource = entity.getResource();
        return resourceStorageServiceManager.download(resource);
    }

    @Override
    public void delete(Song entity) throws Exception {
        Resource resource = entity.getResource();
        resourceStorageServiceManager.delete(resource);
    }

    @Override
    public boolean exist(Song entity) {
        Resource resource = entity.getResource();
        return resourceStorageServiceManager.exist(resource);
    }

}
