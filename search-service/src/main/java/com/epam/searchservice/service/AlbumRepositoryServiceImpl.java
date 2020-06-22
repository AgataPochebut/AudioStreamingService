package com.epam.searchservice.service;

import com.epam.searchservice.model.Album;
import org.springframework.stereotype.Service;

@Service
public class AlbumRepositoryServiceImpl extends GenericRepositoryServiceImpl<Album, Long> implements AlbumRepositoryService {
}
