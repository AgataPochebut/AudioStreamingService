package com.epam;

import com.epam.service.repository.ResourceRepositoryService;
import com.epam.service.storage.ResourceStorageService;
import org.springframework.beans.factory.annotation.Autowired;

public class ResourceManager {

    @Autowired
    ResourceRepositoryService resourceRepositoryService;

    @Autowired
    ResourceStorageService resourceStorageService;


}
