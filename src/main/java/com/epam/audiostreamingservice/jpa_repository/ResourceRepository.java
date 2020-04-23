package com.epam.audiostreamingservice.jpa_repository;

import com.epam.audiostreamingservice.model.Resource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends GenericRepository<Resource, Long> {

    Optional<Resource> findByChecksum(String checksum);

}
