package com.it.songservice.service.index;

import com.it.songservice.feign.index.SongIndexClient;
import com.it.songservice.model.Song;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
@Slf4j
public class SongIndexServiceImpl implements SongIndexService {

    @Autowired
    private SongIndexClient indexClient;

    @Override
    public void save(Song entity) {
        indexClient.save(entity);
    }

    @Override
    public void delete(Song entity) {
        indexClient.delete(entity.getId());
    }
}
