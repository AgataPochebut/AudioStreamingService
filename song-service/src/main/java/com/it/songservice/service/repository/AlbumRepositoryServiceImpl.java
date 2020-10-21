package com.it.songservice.service.repository;

import com.it.songservice.model.Album;
import com.it.songservice.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlbumRepositoryServiceImpl extends GenericRepositoryServiceImpl<Album, Long> implements AlbumRepositoryService {

    @Autowired
    private AlbumRepository repository;

    @Override
    public Album findByName(String s) {
        return repository.findByName(s).orElse(null);
    }
}
