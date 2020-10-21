package com.it.storageservice.service.storage;

import com.it.storageservice.annotation.Decorate;
import com.it.storageservice.model.Resource;
import com.it.storageservice.service.storage.decorator.ResourceIORetryDecorator;
import com.it.storageservice.service.storage.decorator.ResourceCacheDecorator;
import com.it.storageservice.service.storage.decorator.ResourceDBDecorator;
import com.it.storageservice.service.storage.decorator.ResourceDedupingDecorator;

@Decorate(decorations = {ResourceIORetryDecorator.class, ResourceDBDecorator.class, ResourceDedupingDecorator.class, ResourceCacheDecorator.class})
public interface ResourceStorageService<T extends Resource> {

    T upload(org.springframework.core.io.Resource source, String name) throws Exception;

    org.springframework.core.io.Resource download(T resource) throws Exception;

    void delete(T resource) throws Exception;

    boolean exist(T resource);

    boolean supports(Class<? extends Resource> clazz);
}
