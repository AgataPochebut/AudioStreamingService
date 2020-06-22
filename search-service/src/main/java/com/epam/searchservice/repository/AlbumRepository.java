package com.epam.searchservice.repository;

import com.epam.searchservice.model.Album;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends GenericRepository<Album, Long> {
}
