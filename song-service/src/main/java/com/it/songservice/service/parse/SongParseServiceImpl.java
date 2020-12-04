package com.it.songservice.service.parse;

import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import de.odysseus.ithaka.audioinfo.AudioInfo;
import de.odysseus.ithaka.audioinfo.mp3.ID3v2Info;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Primary
@Slf4j
public class SongParseServiceImpl implements SongParseService {

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private Mapper mapper;

    @Override
    public Song parse(Resource resource) throws Exception {
        org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);
        BufferedInputStream input = new BufferedInputStream(source.getInputStream());

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
