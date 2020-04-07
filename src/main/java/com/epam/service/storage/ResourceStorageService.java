package com.epam.service.storage;

import com.epam.model.Resource;
import com.epam.model.StorageTypes;
import org.springframework.core.io.FileUrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ResourceStorageService {

    Resource upload(org.springframework.core.io.Resource resource) throws Exception;

    org.springframework.core.io.Resource download(Resource resource) throws Exception;

//    org.springframework.core.io.Resource download(Long id);

    void delete(Resource resource);

//    void delete(Long id);

    boolean exist(Resource resource);

//    boolean exist(Long id);

    String test();
}
