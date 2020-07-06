package com.epam.resourceservice.service.storage;

import com.epam.resourceservice.model.Resource;
import com.epam.storageservice.service.ResourceStorageFactory;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceStorageServiceImpl implements ResourceStorageService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    @Override
    public Resource upload(com.epam.storageservice.model.Resource resource) throws Exception {
        com.epam.resourceservice.model.Resource resource1 =
                mapper.map(resource, com.epam.resourceservice.model.Resource.class);
        return resource1;
    }

    @Override
    public com.epam.storageservice.model.Resource download(Resource resource) throws Exception {
        com.epam.storageservice.model.Resource resource1 =
                mapper.map(resource, com.epam.storageservice.model.Resource.class);
        return resource1;
    }

    @Override
    public void delete(Resource resource) {
        com.epam.storageservice.model.Resource resource1 =
                mapper.map(resource, com.epam.storageservice.model.Resource.class);
        resourceStorageFactory.getService().delete(resource1);
    }

    @Override
    public String test() {
        return null;
    }
}
