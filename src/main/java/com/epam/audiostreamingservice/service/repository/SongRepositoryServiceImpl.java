package com.epam.audiostreamingservice.service.repository;

import com.epam.audiostreamingservice.model.Song;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SongRepositoryServiceImpl extends GenericServiceImpl<Song, Long> implements SongRepositoryService {
}
