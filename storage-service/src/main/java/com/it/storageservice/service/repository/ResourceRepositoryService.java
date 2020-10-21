package com.it.storageservice.service.repository;

import com.it.songservice.model.Resource;

public interface ResourceRepositoryService extends GenericRepositoryService<Resource, Long> {

    Resource findByChecksum(String s);

}
