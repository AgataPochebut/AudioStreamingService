package com.epam.service.index;

import com.epam.model.Song;
import org.springframework.stereotype.Service;

@Service
public class SongESRepositoryService extends GenericESRepositoryService<Song, Long> implements SongRepositoryService {
}
