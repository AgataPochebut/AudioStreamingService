package com.epam.songservice.service.storage.resource;

import com.epam.songservice.model.Resource;

public interface ResourceStorageService<T extends Resource> {

    T upload(org.springframework.core.io.Resource source, String name) throws Exception;

    org.springframework.core.io.Resource download(T resource) throws Exception;

    void delete(T resource) throws Exception;

    boolean exist(T resource);

    boolean supports(Class<?> resource);
}
