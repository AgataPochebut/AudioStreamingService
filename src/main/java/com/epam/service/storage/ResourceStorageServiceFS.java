package com.epam.service.storage;

import com.epam.annotation.Decorate;
import com.epam.annotation.StorageType;
import com.epam.model.Resource;
import com.epam.model.StorageTypes;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


@Decorate(ResourceStorageDecorator.class)
@StorageType(StorageTypes.FS)
@Service
public class ResourceStorageServiceFS implements ResourceStorageService {

    @Value("${fs.defaultFolder}")
    private String defaultBaseFolder;

    @Override
    public Resource upload(org.springframework.core.io.Resource source) throws IOException {
        File file;
        if (source.isFile()) {
            file = source.getFile();
        } else {
            file = new File(defaultBaseFolder, source.getFilename());
            file.getParentFile().mkdirs();
            FileCopyUtils.copy(source.getInputStream(), new FileOutputStream(file));
        }

        return Resource.builder()
                .path(file.getAbsolutePath())
                .parent(file.getParent())
                .name(file.getName())
                .size(file.length())
                .checksum(DigestUtils.md5Hex(new FileInputStream(file)))
                .storageTypes(StorageTypes.FS)
                .build();
    }

    @Override
    public org.springframework.core.io.Resource download(Resource resource) {
        return new FileSystemResource(resource.getPath());
    }

//    @Override
//    public org.springframework.core.io.Resource download(Long id) {
//        Resource resource = repositoryService.findById(id);
//        return download(resource);
//    }

    @Override
    public void delete(Resource resource) {
        new File(resource.getPath()).delete();
    }

//    @Override
//    public void delete(Long id) {
//        Resource resource = repositoryService.findById(id);
//        delete(resource);
//    }

    @Override
    public boolean exist(Resource resource) {
        return new File(resource.getPath()).exists();
    }

//    @Override
//    public boolean exist(Long id) {
//        Resource resource = repositoryService.findById(id);
//        return exist(resource);
//    }

    @Override
    public String test() {
        return "FS";
    }

}
