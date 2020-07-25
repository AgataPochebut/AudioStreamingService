package com.epam.songservice.service.storage.Song;

import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.repository.AlbumRepositoryService;
import com.epam.songservice.service.storage.Resource.ResourceStorageFactory;
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
import java.util.Map;
import java.util.stream.Collectors;


@Decorate(SongStorageDecorator.class)
@Service
public class SongStorageServiceImpl implements SongStorageService {

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    @Autowired
    private Mapper mapper;

    private static AlbumRepositoryService repositoryService;

    @Override
    public Song upload(Resource resource) throws Exception {
        try {
            org.springframework.core.io.Resource source = resourceStorageFactory.getService().download(resource);

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
            entity.setResource(resource);

            return entity;
        } catch (Exception e) {
            resourceStorageFactory.getService().delete(resource);
            throw new Exception("Upload exc");
        }
    }

    @Override
    public Resource download(Song entity) throws Exception {
        return entity.getResource();
    }

    @Override
    public void delete(Song entity) throws Exception {
        Resource resource = entity.getResource();
        resourceStorageFactory.getService().delete(resource);
    }

    @Override
    public boolean exist(Song entity) {
        Resource resource = entity.getResource();
        return resourceStorageFactory.getService().exist(resource);
    }

    @Override
    public String test() {
        return "Song";
    }

}
