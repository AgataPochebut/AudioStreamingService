package com.epam.playservice.service.repository;

import com.epam.playservice.model.Song;
import com.epam.playservice.repository.SongRepository;
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
