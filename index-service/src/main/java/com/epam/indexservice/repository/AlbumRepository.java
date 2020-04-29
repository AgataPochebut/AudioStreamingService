package com.epam.indexservice.repository;

import com.epam.indexservice.model.Album;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends GenericRepository<Album, Long> {
}
