package com.epam.audiostreamingservice.jpa_repository;

import com.epam.audiostreamingservice.model.Album;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends GenericRepository<Album, Long> {
}
