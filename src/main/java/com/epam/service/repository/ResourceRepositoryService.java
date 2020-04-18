package com.epam.service.repository;

import com.epam.model.Resource;

public interface ResourceRepositoryService extends GenericService<Resource, Long> {

    Resource findByChecksum(String checksum);

    boolean existByChecksum(String checksum);

}