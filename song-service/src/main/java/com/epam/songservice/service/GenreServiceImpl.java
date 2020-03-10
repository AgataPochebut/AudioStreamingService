package com.epam.songservice.service;

import com.epam.songservice.model.Genre;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GenreServiceImpl extends GenericServiceImpl<Genre, Long> implements GenreService {
}
