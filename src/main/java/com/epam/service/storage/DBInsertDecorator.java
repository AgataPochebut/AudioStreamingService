package com.epam.service.storage;

import com.epam.model.Resource;
import com.epam.service.repository.ResourceRepositoryService;
import org.springframework.web.multipart.MultipartFile;

public class DBInsertDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public DBInsertDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Resource upload(MultipartFile file) throws Exception {
        Resource resource = super.upload(file);
        return repositoryService.save(resource);
    }

    @Override
    public String make() {
        return super.make() + "DBInsert";
    }
}
