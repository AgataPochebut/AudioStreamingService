package com.epam.songservice.service;

import com.epam.songservice.model.Song;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SongServiceImpl extends GenericServiceImpl<Song, Long> implements SongService {
}
