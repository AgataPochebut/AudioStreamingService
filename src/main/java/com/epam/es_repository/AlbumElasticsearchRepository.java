package com.epam.es_repository;

import com.epam.model.Album;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumElasticsearchRepository extends GenericElasticsearchRepository<Album, Long> {
}
