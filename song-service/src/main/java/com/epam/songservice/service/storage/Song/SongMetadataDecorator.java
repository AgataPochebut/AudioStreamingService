//package com.epam.songservice.service.storage.Song;
//
//import com.epam.songservice.model.Song;
//import com.epam.storageservice.model.Resource;
//import com.epam.storageservice.service.ResourceStorageFactory;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.tika.metadata.Metadata;
//import org.apache.tika.parser.ParseContext;
//import org.apache.tika.parser.Parser;
//import org.apache.tika.parser.mp3.Mp3Parser;
//import org.dozer.Mapper;
//import org.springframework.transaction.annotation.Transactional;
//import org.xml.sax.ContentHandler;
//import org.xml.sax.helpers.DefaultHandler;
//
//import java.io.InputStream;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Transactional
//public class SongMetadataDecorator extends SongStorageDecorator {
//
//    private Mapper mapper;
//
//    private ResourceStorageFactory resourceStorageFactory;
//
//    public SongMetadataDecorator(SongStorageService storageService, Mapper mapper) {
//        super(storageService);
//        this.mapper = mapper;
//    }
//
//    @Override
//    public Song upload1(Resource resource) throws Exception {
//        Song entity = super.upload1(resource);
//
//        org.springframework.core.io.Resource source = resourceStorageFactory.getService().download(resource);
//
//        InputStream input = source.getInputStream();
//        ContentHandler handler = new DefaultHandler();
//        Metadata metadata = new Metadata();
//        Parser parser = new Mp3Parser();
//        ParseContext parseCtx = new ParseContext();
//        parser.parse(input, handler, metadata, parseCtx);
//
//        Map<String, Object> metadataMap = new HashMap<>();
//        if (metadata.get("title") != null && !metadata.get("title").isEmpty()) {
//            metadataMap.put("Title", metadata.get("title"));
//        } else {
//            metadataMap.put("Title", FilenameUtils.removeExtension(name));
//        }
//        metadataMap.put("Year", metadata.get("xmpDM:releaseDate"));
//
//        if (metadata.get("xmpDM:album") != null && !metadata.get("xmpDM:album").isEmpty()) {
//            Map<String, Object> albumMap = new HashMap<>();
//            albumMap.put("Title", metadata.get("xmpDM:album"));
//            albumMap.put("Year", metadata.get("xmpDM:releaseDate"));
//
//            if (metadata.get("xmpDM:artist") != null && !metadata.get("xmpDM:artist").isEmpty()) {
//                albumMap.put("Artists", Arrays.stream(metadata.get("xmpDM:artist").split(", "))
//                        .map(a -> {
//                            Map<String, Object> artistMap = new HashMap<>();
//                            artistMap.put("Name", a);
//
//                            if (metadata.get("xmpDM:genre") != null && !metadata.get("xmpDM:genre").isEmpty()) {
//                                artistMap.put("Genres", Arrays.stream(metadata.get("xmpDM:genre").split(", "))
//                                        .map(g -> {
//                                            Map<String, Object> genreMap = new HashMap<>();
//                                            genreMap.put("Name", g);
//                                            return genreMap;
//                                        })
//                                        .collect(Collectors.toSet()));
//                            }
//
//                            return artistMap;
//                        })
//                        .collect(Collectors.toSet()));
//            }
//
//            metadataMap.put("Album", albumMap);
//        }
//
//        mapper.map(mapper.map(metadataMap, Song.class), entity);
//
//        return entity;
//    }
//
//    @Override
//    public String test() {
//        return super.test() + " Meta";
//    }
//
//}
