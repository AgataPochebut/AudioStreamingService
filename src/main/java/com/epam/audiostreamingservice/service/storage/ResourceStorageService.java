package com.epam.audiostreamingservice.service.storage;

import com.epam.audiostreamingservice.model.Resource;

public interface ResourceStorageService {

    org.springframework.core.io.Resource download(Resource resource) throws Exception;

//    org.springframework.core.io.Resource download(Long id);

    Resource upload(org.springframework.core.io.Resource source) throws Exception;

    void delete(Resource resource);

//    void delete(Long id);

    boolean exist(Resource resource);

//    boolean exist(Long id);

    String test();
}
