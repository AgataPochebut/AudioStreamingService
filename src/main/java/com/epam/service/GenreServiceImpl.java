package com.epam.service;

import com.epam.model.Artist;
import com.epam.model.Genre;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GenreServiceImpl extends GenericServiceImpl<Genre, Long> implements GenreService {
}
