package com.epam.searchservice.service;

import com.epam.searchservice.model.Song;
import org.springframework.stereotype.Service;

@Service
public class SongRepositoryServiceImpl extends GenericRepositoryServiceImpl<Song, Long> implements SongRepositoryService {
}
