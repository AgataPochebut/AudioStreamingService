package com.epam.songservice.service.storage;

import com.epam.songservice.model.Resource;
import com.epam.songservice.service.repository.ResourceRepositoryService;

public class DBInsertDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public DBInsertDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Resource resource = super.upload(source, name);
        return repositoryService.save(resource);
    }

    @Override
    public void delete(Resource resource) {
        super.delete(resource);
        repositoryService.deleteById(resource.getId());
    }

//    @Override
//    public void delete(Long id) {
//        super.delete(id);
//        repositoryService.deleteById(id);
//    }

    @Override
    public String test() {
        return super.test() + " DBInsert";
    }

}
