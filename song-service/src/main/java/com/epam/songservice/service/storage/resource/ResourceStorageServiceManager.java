package com.epam.songservice.service.storage.resource;

import com.epam.songservice.model.Resource;
import com.epam.songservice.model.StorageTypes;
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

        Resource result = null;

        try {
            result = provider.upload(source, name);

            if (result != null) {
                return result;
            }
        } catch (Exception e) {

        }
        throw new Exception("lalala");
    }

    public org.springframework.core.io.Resource download(Resource resource) throws Exception {

        org.springframework.core.io.Resource result = null;

        for (ResourceStorageService provider : providers.values()) {
            if (!provider.supports(resource.getClass())) {
                continue;
            }

            try {
                result = provider.download(resource);

                if (result != null) {
                    return result;
                }
            } catch (Exception e) {

            }
        }
        throw new Exception("lalala");
    }

    public void delete(Resource resource) throws Exception{
        for (ResourceStorageService provider : providers.values()) {
            if (!provider.supports(resource.getClass())) {
                continue;
            }

            try {
                if (provider.exist(resource)) {
                    provider.delete(resource);
                    return;
                }
            } catch (Exception e) {

            }
        }
        throw new Exception("lalala");
    }

    public boolean exist(Resource resource) {
        boolean result = false;

        for (ResourceStorageService provider : providers.values()) {
            if (!provider.supports(resource.getClass())) {
                continue;
            }

            try {
                result = provider.exist(resource);

                if (result) {
                    return result;
                }
            } catch (Exception e) {

            }
        }
        return result;
    }

}
