package com.epam.service.search;

import com.epam.model.Song;
import org.springframework.stereotype.Service;

@Service
public class SongSearchServiceImpl extends SearchServiceImpl<Song, Long> implements SongSearchService{
}
