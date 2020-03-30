package com.epam.service.storage;

import com.epam.annotation.Decorate;
import com.epam.annotation.StorageType;
import com.epam.model.StorageTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//регить в фактори мапу storagetype - сервис
//в фабрике в методе getservice отдавать сервис нужного типа
@Component
public class ResourceStorageFactory {

    private static final Map<StorageTypes, ResourceStorageService> serviceCache = new HashMap<>();

//    @Autowired
//    public ResourceStorageServiceFactory(List<ResourceStorageService> services) {
//        for (ResourceStorageService service : services) {
//            //тут уже декорированный, нет storagetype
//            if (service.getClass().isAnnotationPresent(StorageType.class)) {
//                serviceCache.put(service.getClass().getAnnotation(StorageType.class).storageType(), service);
//            }
//        }
//    }

    public void registerService(StorageTypes storageType, ResourceStorageService storageService)
    {
        serviceCache.put(storageType, storageService);
    }

    public ResourceStorageService getService(){
        return serviceCache.get(StorageTypes.FS);
    };

}
