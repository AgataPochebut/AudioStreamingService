package com.epam.songservice.service.repository;

import com.epam.songservice.model.Resource;

public interface ResourceRepositoryService extends GenericService<Resource, Long> {

    Resource findByChecksum(String s);

}
