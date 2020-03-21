package com.epam.service.repository;

import com.epam.model.Song;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SongServiceImpl extends GenericServiceImpl<Song, Long> implements SongService {
}
