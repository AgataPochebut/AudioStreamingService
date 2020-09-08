package com.it.songservice.service.repository;

import com.it.songservice.model.Artist;
import com.it.songservice.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArtistRepositoryServiceImpl extends GenericRepositoryServiceImpl<Artist, Long> implements ArtistRepositoryService {

    @Autowired
    private ArtistRepository repository;

    @Override
    public Artist findByName(String s) {
        return repository.findByName(s).orElse(null);
    }
}
