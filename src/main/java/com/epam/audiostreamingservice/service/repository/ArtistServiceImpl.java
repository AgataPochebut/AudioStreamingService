package com.epam.audiostreamingservice.service.repository;

import com.epam.audiostreamingservice.model.Artist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArtistServiceImpl extends GenericServiceImpl<Artist, Long> implements ArtistService {
}
