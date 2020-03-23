package com.epam.service.storage;

import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface StorageService {

    File downloadFile(String path);

    File uploadFile(MultipartFile file) throws IOException;

}
