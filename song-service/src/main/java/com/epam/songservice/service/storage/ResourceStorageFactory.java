package com.epam.songservice.service.storage;

import com.epam.songservice.model.StorageTypes;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//регить в фактори мапу storagetype - сервис
//в фабрике в методе getservice отдавать сервис нужного типа
@Component
public class ResourceStorageFactory {

    private static final Map<StorageTypes, ResourceStorageService> serviceCache = new HashMap<>();

//    @Autowired
//    public ResourceStorageFactory(List<ResourceStorageService> services) {
//        for (ResourceStorageService service : services) {
//            //тут уже декорированный, нет storagetype
//            if (service.getClass().isAnnotationPresent(StorageType.class)) {
//                serviceCache.put(service.getClass().getAnnotation(StorageType.class).value(), service);
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
