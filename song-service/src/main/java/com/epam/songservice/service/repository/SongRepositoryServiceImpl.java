package com.epam.songservice.service.repository;

import com.epam.songservice.model.Song;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SongRepositoryServiceImpl extends GenericServiceImpl<Song, Long> implements SongRepositoryService {
}
