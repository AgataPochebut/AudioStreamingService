package com.epam.songservice.service.storage.resource;

import com.epam.songservice.model.StorageTypes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//регить в фактори мапу storagetype - сервис
//в фабрике в методе getservice отдавать сервис нужного типа
@Component
public class ResourceStorageFactory {

    @Value("${storage.type}")
    private String storageType;

    private static final Map<StorageTypes, ResourceStorageService> serviceCache = new HashMap<>();

    public void registerService(StorageTypes storageType, ResourceStorageService storageService)
    {
        serviceCache.put(storageType, storageService);
    }

    public ResourceStorageService getService(){
        return serviceCache.get(StorageTypes.valueOf(storageType));
    };

    public ResourceStorageService getService(StorageTypes storageType){
        return serviceCache.get(storageType);
    };

}
