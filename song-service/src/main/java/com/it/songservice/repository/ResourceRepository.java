package com.it.songservice.repository;

import com.it.songservice.model.Resource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceRepository extends GenericRepository<Resource, Long> {

    Optional<Resource> findByChecksum(String s);

}
