package com.it.storageservice.service.storage;

import com.it.storageservice.model.Resource;
import com.it.storageservice.model.StorageTypes;
import com.it.storageservice.exception.DownloadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResourceStorageServiceManager {

    @Value("${storage.type}")
    private String storageType;

    private Map<StorageTypes, ResourceStorageService> providers = new HashMap<>();

    public void registerService(StorageTypes storageType, ResourceStorageService storageService)
    {
        providers.put(storageType, storageService);
    }

    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        ResourceStorageService provider = providers.get(StorageTypes.valueOf(storageType));
        return provider.upload(source, name);
    }

    public org.springframework.core.io.Resource download(Resource resource) throws Exception {
        for (ResourceStorageService provider : providers.values()) {
            if (!provider.supports(resource.getClass())) {
                continue;
            }
            return provider.download(resource);
        }

        throw new DownloadException("No siutable service");
    }

    public void delete(Resource resource) throws Exception{
        for (ResourceStorageService provider : providers.values()) {
            if (!provider.supports(resource.getClass())) {
                continue;
            }
            provider.delete(resource);
            if(!provider.exist(resource)) {
                return;
            }
        }

        throw new Exception("No siutable service");
    }

    public boolean exist(Resource resource) throws Exception {
        for (ResourceStorageService provider : providers.values()) {
            if (!provider.supports(resource.getClass())) {
                continue;
            }
            return provider.exist(resource);
        }
        throw new Exception("No siutable service");
    }

}
