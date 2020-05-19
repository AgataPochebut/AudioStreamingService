package com.epam.playservice.service;

import com.epam.playservice.model.Playlist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaylistServiceImpl extends GenericServiceImpl<Playlist, Long> implements PlaylistService {


}
