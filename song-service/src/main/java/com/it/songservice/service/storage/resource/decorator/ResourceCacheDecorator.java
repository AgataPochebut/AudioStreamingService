package com.it.songservice.service.storage.resource.decorator;

import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import org.apache.commons.io.IOUtils;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.cache.RedisCache;

public class ResourceCacheDecorator extends ResourceStorageDecorator {

    private CacheManager cacheManager;
    RedisCache cache;

    public ResourceCacheDecorator(ResourceStorageService storageService, CacheManager cacheManager) {
        super(storageService);
        this.cacheManager = cacheManager;
    }

    @Override
    public org.springframework.core.io.Resource download(Resource resource) throws Exception {

        try {
            byte[] cachedSource = cacheManager.getCache("resources").get(resource.getId(), byte[].class);
            if (cachedSource != null) {
                return new ByteArrayResource(cachedSource);
            }
        } catch (Exception e) {

        }

        org.springframework.core.io.Resource source = super.download(resource);

        try {
            byte[] cachedSource = IOUtils.toByteArray(source.getInputStream());
            cacheManager.getCache("resources").put(resource.getId(), cachedSource);
        } catch (Exception e) {

        }

        return source;
    }

    @Override
    public void delete(Resource resource) throws Exception {
        try {
            cacheManager.getCache("resources").evictIfPresent(resource.getId());
        }
        catch (Exception e) {

        }

        super.delete(resource);
    }
}
