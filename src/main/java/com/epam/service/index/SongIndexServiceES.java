package com.epam.service.index;

import com.epam.model.Song;
import org.springframework.stereotype.Service;

@Service
public class SongIndexServiceES extends GenericIndexServiceES<Song, Long> implements SongIndexService {
}
