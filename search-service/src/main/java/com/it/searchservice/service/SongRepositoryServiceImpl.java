package com.it.searchservice.service;

import com.it.searchservice.model.Song;
import org.springframework.stereotype.Service;

@Service
public class SongRepositoryServiceImpl extends GenericRepositoryServiceImpl<Song, Long> implements SongRepositoryService {
}
