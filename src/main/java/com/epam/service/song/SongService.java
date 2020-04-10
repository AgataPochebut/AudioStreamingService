package com.epam.service.song;

import com.epam.model.Song;
import org.springframework.core.io.Resource;

import java.util.List;

public interface SongService {

    List<Song> getAll();
    Song get(Long id);
    Resource download(Long id) throws Exception;
    Song upload(Resource source) throws Exception;
    List<Song> uploadZip(Resource source) throws Exception;
    void delete(Long id);

}
