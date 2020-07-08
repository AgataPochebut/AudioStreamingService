package com.epam.songservice.service.storage.Resource;

import com.epam.songservice.model.Resource;
import com.epam.songservice.service.repository.ResourceRepositoryService;

public class ResourceDBDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public ResourceDBDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource entity = super.upload(source, name);
        if (repositoryService.findByChecksum(entity.getChecksum())==null) {
            entity = repositoryService.save(entity);
            return entity;
        } else {
            super.delete(entity);
            throw new Exception("Exist");
        }
    }

    @Override
    public void delete(Resource resource) {
        super.delete(resource);
        repositoryService.deleteById(resource.getId());
    }

    @Override
    public String test() {
        return super.test() + " DBInsert";
    }

}
