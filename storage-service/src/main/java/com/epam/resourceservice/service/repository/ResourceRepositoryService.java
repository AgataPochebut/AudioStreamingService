package com.epam.resourceservice.service.repository;

import com.epam.resourceservice.model.Resource;

public interface ResourceRepositoryService extends GenericService<Resource, Long> {

    Resource findByChecksum(String checksum);

    boolean existByChecksum(String checksum);

}
