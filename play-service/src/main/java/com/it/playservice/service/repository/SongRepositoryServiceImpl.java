package com.it.playservice.service.repository;

import com.it.playservice.model.Song;
import com.it.playservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SongRepositoryServiceImpl extends GenericRepositoryServiceImpl<Song, Long> implements SongRepositoryService {

    @Autowired
    private SongRepository repository;

    @Override
    public Song findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
