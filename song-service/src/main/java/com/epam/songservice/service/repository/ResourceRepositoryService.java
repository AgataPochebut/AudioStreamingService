package com.epam.songservice.service.repository;

import com.epam.songservice.model.Resource;

public interface ResourceRepositoryService extends GenericRepositoryService<Resource, Long> {

    Resource findByChecksum(String s);

}
