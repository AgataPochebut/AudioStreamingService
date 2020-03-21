package com.epam.service.repository;

import com.epam.model.Album;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlbumServiceImpl extends GenericServiceImpl<Album, Long> implements AlbumService {
}
