package com.epam.storageservice.service;

import com.epam.storageservice.model.StorageTypes;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//регить в фактори мапу storagetype - сервис
//в фабрике в методе getservice отдавать сервис нужного типа
@Component
public class ResourceStorageFactory {

    private static final Map<StorageTypes, ResourceStorageService> serviceCache = new HashMap<>();

//    @Autowired
//    private ResourceRepositoryService repositoryService;
//
////    @Autowired
////    private ConversionClient conversionService;
////
////    //or
////
////    @LoadBalanced
////    @Bean
////    RestTemplate restTemplate() {
////        return new RestTemplate();
////    }
//
//    @Autowired
//    private JmsTemplate jmsTemplate;
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    @Autowired
//    public ResourceStorageFactory(List<ResourceStorageService> services) {
//        for (ResourceStorageService bean : services) {
//
//            ResourceStorageService newbean = bean;
//            if (bean.getClass().isAnnotationPresent(Decorate.class)
//                    && bean.getClass().getAnnotation(Decorate.class).value() == ResourceStorageDecorator.class) {
//                newbean = new IORetryDecorator(newbean);
//                newbean = new DBInsertDecorator(newbean, repositoryService);
//                newbean = new DedupingDecorator(newbean, repositoryService);
//                newbean = new ConversionDecorator(newbean, jmsTemplate);
//                newbean = new CacheDecorator(newbean, cacheManager);
//            }
//            serviceCache.put(bean.getClass().getAnnotation(StorageType.class).value(), newbean);
//
////            //тут уже декорированный, нет storagetype
////            if (bean.getClass().isAnnotationPresent(StorageType.class)) {
////                serviceCache.put(bean.getClass().getAnnotation(StorageType.class).value(), bean);
////            }
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
