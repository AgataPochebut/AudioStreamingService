package com.epam.repository;

import com.epam.model.Resource;
import com.epam.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends GenericRepository<Resource, Long> {
}
