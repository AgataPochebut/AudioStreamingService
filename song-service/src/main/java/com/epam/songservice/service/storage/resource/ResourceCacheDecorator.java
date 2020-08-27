package com.epam.songservice.service.storage.resource;

import com.epam.songservice.model.Resource;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ByteArrayResource;

public class ResourceCacheDecorator extends ResourceStorageDecorator {

    private CacheManager cacheManager;

    public ResourceCacheDecorator(ResourceStorageService storageService, CacheManager cacheManager) {
        super(storageService);
        this.cacheManager = cacheManager;
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

    @Override
    public String test() {
        return super.test() + " Cache";
    }

}
