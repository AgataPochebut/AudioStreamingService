package com.epam.service.storage;

import com.epam.model.Resource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface StorageService {

    Resource upload(MultipartFile file) throws IOException;

    org.springframework.core.io.Resource download(Resource resource);

    void delete(Resource resource);
}
