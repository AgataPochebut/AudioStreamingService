package com.epam.service.index;

import com.epam.model.Song;
import org.springframework.stereotype.Service;

@Service
public class SongRepositoryServiceES extends GenericRepositoryServiceES<Song, Long> implements SongRepositoryService {
}
