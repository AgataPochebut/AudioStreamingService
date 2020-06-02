package com.epam.storageservice.service.storage;

import com.epam.storageservice.model.Resource;
import com.epam.storageservice.service.repository.ResourceRepositoryService;

public class CleanupDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public CleanupDecorator(ResourceStorageService service, ResourceRepositoryService repositoryService){
        super(service);
        this.repositoryService = repositoryService;
    }

    @Override
    public Resource upload(final org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = super.upload(source, name);
        if(!super.exist(resource)){
            repositoryService.delete(resource);
            throw new Exception("Error in clenup decorator");
        }
        return resource;
    }
}
