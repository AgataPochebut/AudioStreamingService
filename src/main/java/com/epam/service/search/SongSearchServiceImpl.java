package com.epam.service.search;

import com.epam.model.Song;
import org.springframework.stereotype.Service;

@Service
public class SongSearchServiceImpl extends GenericSearchServiceImpl<Song, Long> implements SongSearchService {
}
