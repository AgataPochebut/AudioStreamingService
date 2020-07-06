package com.epam.resourceservice.service.storage;

import com.epam.resourceservice.service.repository.ResourceRepositoryService;
import com.epam.resourceservice.model.Resource;

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
