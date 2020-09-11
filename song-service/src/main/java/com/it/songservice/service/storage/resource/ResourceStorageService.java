package com.it.songservice.service.storage.resource;

import com.it.songservice.annotation.Decorate;
import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.decorator.ResourceCacheDecorator;
import com.it.songservice.service.storage.resource.decorator.ResourceDBDecorator;
import com.it.songservice.service.storage.resource.decorator.ResourceDedupingDecorator;
import com.it.songservice.service.storage.resource.decorator.ResourceIORetryDecorator;

@Decorate(decorations = {ResourceIORetryDecorator.class, ResourceDBDecorator.class, ResourceDedupingDecorator.class, ResourceCacheDecorator.class})
public interface ResourceStorageService<T extends Resource> {

    T upload(org.springframework.core.io.Resource source, String name) throws Exception;

    org.springframework.core.io.Resource download(T resource) throws Exception;

    void delete(T resource) throws Exception;

    boolean exist(T resource);

    boolean supports(Class<? extends Resource> clazz);
}
