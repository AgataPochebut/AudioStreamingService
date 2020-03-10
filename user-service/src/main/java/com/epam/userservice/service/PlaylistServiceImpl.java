package com.epam.userservice.service;

import com.epam.songservice.model.Album;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaylistServiceImpl extends GenericServiceImpl<Album, Long> implements PlaylistService {
}
