package com.epam.indexservice.service;

import com.epam.indexservice.model.Song;
import org.springframework.stereotype.Service;

@Service
public class SongIndexServiceES extends GenericIndexServiceES<Song, Long> implements SongIndexService {
}
