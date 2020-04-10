package com.epam.jpa_repository;

import com.epam.model.Album;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends GenericRepository<Album, Long> {
}
