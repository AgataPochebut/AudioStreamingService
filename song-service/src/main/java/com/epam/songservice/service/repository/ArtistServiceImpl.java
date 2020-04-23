package com.epam.songservice.service.repository;

import com.epam.songservice.model.Artist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArtistServiceImpl extends GenericServiceImpl<Artist, Long> implements ArtistService {
}
