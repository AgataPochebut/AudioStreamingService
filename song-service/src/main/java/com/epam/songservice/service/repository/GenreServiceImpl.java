package com.epam.songservice.service.repository;

import com.epam.songservice.model.Genre;
import com.epam.songservice.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GenreServiceImpl extends GenericServiceImpl<Genre, Long> implements GenreService {

    @Autowired
    private GenreRepository repository;

    @Override
    public Genre findByName(String s) {
        return repository.findByName(s).orElse(null);
    }
}
