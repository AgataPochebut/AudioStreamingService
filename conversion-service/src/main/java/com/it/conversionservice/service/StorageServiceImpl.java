package com.it.conversionservice.service;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


@Service
public class StorageServiceImpl implements StorageService {

    @Value("${fs.tempFolder}")
    private String defaultBaseFolder;

    @Override
    public File upload(org.springframework.core.io.Resource source, String name) throws Exception {
        int count = 0;
        String key = name;
        while (Files.exists(Paths.get(defaultBaseFolder, key))){
            count++;
            key = FilenameUtils.removeExtension(name) + " ("+ count + ")" + "." + FilenameUtils.getExtension(name);
        }

        Files.createDirectories(Paths.get(defaultBaseFolder, key).getParent());
        Files.copy(source.getInputStream(), Paths.get(defaultBaseFolder, key));

        return Paths.get(defaultBaseFolder, key).toFile();
    }

    @Override
    public org.springframework.core.io.Resource download(File resource) throws IOException {
        return new FileSystemResource(resource);
    }

    @Override
    public void delete(File resource) {
        resource.delete();
    }

    @Override
    public boolean exist(File resource) {
        return resource.exists();
    }

}
