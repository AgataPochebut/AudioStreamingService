package com.epam.audiostreamingservice.service.repository;

import com.epam.audiostreamingservice.model.Resource;

public interface ResourceRepositoryService extends GenericService<Resource, Long> {

    Resource findByChecksum(String checksum);

    boolean existByChecksum(String checksum);

}
