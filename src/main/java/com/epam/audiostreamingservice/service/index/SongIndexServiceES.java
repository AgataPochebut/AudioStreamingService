package com.epam.audiostreamingservice.service.index;

import com.epam.audiostreamingservice.model.Song;
import org.springframework.stereotype.Service;

@Service
public class SongIndexServiceES extends GenericIndexServiceES<Song, Long> implements SongIndexService {
}
