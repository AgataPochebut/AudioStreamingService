package com.epam.playservice.service;

import com.epam.playservice.feign.SongServiceClient;
import com.epam.playservice.model.Song;
import com.epam.playservice.service.repository.SongRepositoryService;
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
