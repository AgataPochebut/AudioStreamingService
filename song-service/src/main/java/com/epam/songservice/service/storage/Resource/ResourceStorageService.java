package com.epam.songservice.service.storage.Resource;

import com.epam.songservice.model.Resource;

public interface ResourceStorageService {

    Resource upload(org.springframework.core.io.Resource source, String name) throws Exception;

    org.springframework.core.io.Resource download(Resource resource) throws Exception;

    void delete(Resource resource);

    boolean exist(Resource resource);

    String test();
}
