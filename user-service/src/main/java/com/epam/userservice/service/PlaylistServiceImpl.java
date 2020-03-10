package com.epam.userservice.service;

import com.epam.songservice.model.Album;
import com.epam.userservice.model.Playlist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaylistServiceImpl extends GenericServiceImpl<Playlist, Long> implements PlaylistService {
}
