package com.it.playservice.service;

import com.it.playservice.feign.song.SongServiceClient;
import com.it.playservice.model.Song;
import com.it.playservice.service.repository.SongRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongService {

    @Autowired
    private SongServiceClient songServiceClient;

    @Autowired
    private SongRepositoryService songRepositoryService;

    public Song get(Long id) throws Exception {
        Song entity = songRepositoryService.findById(id);
        if(entity == null) {
            entity = songServiceClient.getSong(id).getBody();
            entity = songRepositoryService.save(entity);
        }
        return entity;
    }

    public void delete(Song entity) throws Exception {
        songRepositoryService.delete(entity);
    }
}
