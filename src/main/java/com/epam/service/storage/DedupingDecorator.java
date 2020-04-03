package com.epam.service.storage;

import com.epam.model.Resource;
import com.epam.service.repository.ResourceRepositoryService;
import org.apache.commons.codec.digest.DigestUtils;

public class DedupingDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public DedupingDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source) throws Exception {

        Resource resource = repositoryService.findByChecksum(DigestUtils.md5Hex(source.getInputStream()));
        if(resource!=null) return resource;
        else return super.upload(source);
    }

    @Override
    public String test() {
        return super.test() + "Deduping";
    }

}
