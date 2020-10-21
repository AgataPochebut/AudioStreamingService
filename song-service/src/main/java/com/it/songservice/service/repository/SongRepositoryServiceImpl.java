package com.it.songservice.service.repository;

import com.it.songservice.model.Song;
import com.it.songservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SongRepositoryServiceImpl extends GenericRepositoryServiceImpl<Song, Long> implements SongRepositoryService {

    @Autowired
    private SongRepository repository;

    @Override
    public Song findByName(String s) {
        return repository.findByName(s).orElse(null);
    }
}
