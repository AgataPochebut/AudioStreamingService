package com.epam.songservice.service.repository;

import com.epam.songservice.model.Album;
import com.epam.songservice.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlbumServiceImpl extends GenericServiceImpl<Album, Long> implements AlbumService {

    @Autowired
    private AlbumRepository repository;

    @Override
    public Album findByTitle(String s) {
        return repository.findByTitle(s).orElse(null);
    }
}
