package com.it.songservice.service.storage.resource.decorator;

import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;

public class ResourceCacheDecorator extends ResourceStorageDecorator {

    private CacheManager cacheManager;

    public ResourceCacheDecorator(ResourceStorageService storageService, CacheManager cacheManager) {
        super(storageService);
        this.cacheManager = cacheManager;
    }

    public ResourceCacheDecorator(ResourceStorageService storageService, ApplicationContext context) {
        super(storageService);
        this.cacheManager = context.getBean(CacheManager.class);
    }

    @Override
    public org.springframework.core.io.Resource download(Resource resource) throws Exception {
        org.springframework.core.io.Resource source = null;
        byte[] cachedSource = cacheManager.getCache("resources").get(resource.getId(), byte[].class);
        if (cachedSource == null){
            source = super.download(resource);
            cacheManager.getCache("resources").put(resource.getId(), source);
        }
        else source = new ByteArrayResource(cachedSource);
        return source;
    }

    @Override
    public void delete(Resource resource) throws Exception {
        cacheManager.getCache("resources").evictIfPresent(resource.getId());
        super.delete(resource);
    }
}
