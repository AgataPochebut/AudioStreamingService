package com.epam.audiostreamingservice.es_repository;

import com.epam.audiostreamingservice.model.Album;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumESRepository extends GenericESRepository<Album, Long> {
}
