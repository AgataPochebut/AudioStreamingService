package com.epam.resourceservice.repository;

import com.epam.resourceservice.model.Resource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends GenericRepository<Resource, Long> {

    Optional<Resource> findByChecksum(String checksum);

}
