package com.epam.resourceservice.service.storage;

import com.epam.resourceservice.model.Resource;

public interface ResourceStorageService {

    Resource upload(com.epam.storageservice.model.Resource source) throws Exception;

    com.epam.storageservice.model.Resource download(Resource resource) throws Exception;

    void delete(Resource resource);

    String test();
}
