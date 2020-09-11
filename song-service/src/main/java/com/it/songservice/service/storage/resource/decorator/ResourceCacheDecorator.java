package com.it.songservice.service.storage.resource.decorator;

import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageService;
import org.apache.commons.io.IOUtils;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ByteArrayResource;

import java.io.InputStream;

public class ResourceCacheDecorator extends ResourceStorageDecorator {

    private CacheManager cacheManager;

    public ResourceCacheDecorator(ResourceStorageService storageService, CacheManager cacheManager) {
        super(storageService);
        this.cacheManager = cacheManager;
    }

    @Override
    public org.springframework.core.io.Resource download(Resource resource) throws Exception {
        org.springframework.core.io.Resource source = null;

        try {
            byte[] cachedSource = cacheManager.getCache("resources").get(resource.getId(), byte[].class);
            if (cachedSource == null) {
                source = super.download(resource);
                InputStream in = source.getInputStream();
                cachedSource = IOUtils.toByteArray(in);
                in.close();
                cacheManager.getCache("resources").put(resource.getId(), cachedSource);
            } else source = new ByteArrayResource(cachedSource);
            return source;
        }
        catch (Exception e) {

        }

        if(source==null) {
            source = super.download(resource);
        }
        return source;
    }

    @Override
    public void delete(Resource resource) throws Exception {
        cacheManager.getCache("resources").evictIfPresent(resource.getId());

        super.delete(resource);
    }
}
