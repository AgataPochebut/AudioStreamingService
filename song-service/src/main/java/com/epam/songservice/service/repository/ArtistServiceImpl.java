package com.epam.songservice.service.repository;

import com.epam.songservice.model.Artist;
import com.epam.songservice.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArtistServiceImpl extends GenericServiceImpl<Artist, Long> implements ArtistService {

    @Autowired
    private ArtistRepository repository;

    @Override
    public Artist findByName(String s) {
        return repository.findByName(s).orElse(null);
    }
}
