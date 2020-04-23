package com.epam.songservice.repository;

import com.epam.songservice.model.Album;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends GenericRepository<Album, Long> {
}
