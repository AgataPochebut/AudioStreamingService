package com.epam.service.storage;

import com.epam.model.Resource;
import com.epam.model.StorageType;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service("StorageServiceFS")
public class StorageServiceFS implements StorageService {

    @Value("${fs.defaultFolder}")
    private String defaultBaseFolder;

    @Override
    public Resource upload(MultipartFile multipartFile) throws IOException {
        File dir = new File(defaultBaseFolder);
        if (!dir.exists()) dir.mkdir();
        File file = new File(defaultBaseFolder, multipartFile.getOriginalFilename());
        if (!file.exists()) file.createNewFile();
        FileCopyUtils.copy(multipartFile.getInputStream(), new FileOutputStream(file));

        return Resource.builder()
                .path(file.getAbsolutePath())
                .parent(file.getParent())
                .name(file.getName())
                .size(file.length())
                .storageType(StorageType.FS)
                .build();
    }

    @Override
    public org.springframework.core.io.Resource download(Resource resource) {
        return new FileSystemResource(resource.getPath());
    }

    @Override
    public void delete(Resource resource) {
        new File(resource.getPath()).delete();
    }

}
