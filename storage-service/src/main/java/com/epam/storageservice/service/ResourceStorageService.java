package com.epam.storageservice.service;//package com.epam.songservice.service.storage.Resource;

import com.epam.storageservice.model.Resource;

public interface ResourceStorageService {

    org.springframework.core.io.Resource download(Resource resource) throws Exception;

    Resource upload(org.springframework.core.io.Resource source, String name) throws Exception;

    void delete(Resource resource);

    boolean exist(Resource resource);

    String test();
}
