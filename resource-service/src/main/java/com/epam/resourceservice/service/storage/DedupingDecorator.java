package com.epam.resourceservice.service.storage;

import com.epam.resourceservice.model.Resource;
import com.epam.resourceservice.service.repository.ResourceRepositoryService;
import org.apache.commons.codec.digest.DigestUtils;

public class DedupingDecorator extends ResourceStorageDecorator {

    private ResourceRepositoryService repositoryService;

    public DedupingDecorator(ResourceStorageService storageService, ResourceRepositoryService repositoryService) {
        super(storageService);
        this.repositoryService = repositoryService;
    }

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        if(repositoryService.existByChecksum(DigestUtils.md5Hex(source.getInputStream()))) throw new Exception("Exist");
        else return super.upload(source, name);
    }

    @Override
    public String test() {
        return super.test() + " Deduping";
    }

}
