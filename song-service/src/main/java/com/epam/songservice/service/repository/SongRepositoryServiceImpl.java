package com.epam.songservice.service.repository;

import com.epam.songservice.model.Song;
import com.epam.songservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SongRepositoryServiceImpl extends GenericServiceImpl<Song, Long> implements SongRepositoryService {

    @Autowired
    private SongRepository repository;

    @Override
    public Song findByTitle(String s) {
        return repository.findByTitle(s).orElse(null);
    }
}
