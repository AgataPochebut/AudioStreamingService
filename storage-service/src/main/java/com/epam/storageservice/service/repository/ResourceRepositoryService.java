package com.epam.storageservice.service.repository;

import com.epam.storageservice.model.Resource;

public interface ResourceRepositoryService extends GenericService<Resource, Long> {

    Resource findByChecksum(String checksum);

    boolean existByChecksum(String checksum);

}
