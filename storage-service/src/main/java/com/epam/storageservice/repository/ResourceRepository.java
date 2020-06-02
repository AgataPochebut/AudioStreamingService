package com.epam.storageservice.repository;

import com.epam.storageservice.model.Resource;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface ResourceRepository extends GenericRepository<Resource, Long> {

    Optional<Resource> findByChecksum(String checksum);

}
