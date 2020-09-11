package com.it.songservice.service.storage.resource;

import com.it.songservice.annotation.StorageType;
import com.it.songservice.model.FSResource;
import com.it.songservice.model.Resource;
import com.it.songservice.model.StorageTypes;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


@StorageType(StorageTypes.FS)
@Service("ResourceStorageServiceFS")
public class ResourceStorageServiceFS implements ResourceStorageService<FSResource> {

    @Value("${fs.defaultFolder}")
    private String defaultBaseFolder;

    @Override
    public FSResource upload(org.springframework.core.io.Resource source, String name) throws Exception {
        int count = 0;
        String key = name;
        while (Files.exists(Paths.get(defaultBaseFolder, key))){
            count++;
            key = FilenameUtils.removeExtension(name) + " ("+ count + ")" + "." + FilenameUtils.getExtension(name);
        }

        Files.copy(source.getInputStream(), Paths.get(defaultBaseFolder, key));

        FSResource resource = new FSResource();
        resource.setName(key);
        resource.setSize(source.contentLength());
        resource.setChecksum(DigestUtils.md5Hex(source.getInputStream()));
        resource.setFolderName(defaultBaseFolder);
        return resource;
    }

    @Override
    public org.springframework.core.io.Resource download(FSResource resource) throws IOException {
        org.springframework.core.io.Resource source = new FileSystemResource(Paths.get(resource.getFolderName(), resource.getName()));
        InputStream in = source.getInputStream();
        byte[] content = IOUtils.toByteArray(in);
        in.close();
        return new ByteArrayResource(content);
    }

    @Override
    public void delete(FSResource resource) throws IOException {
        Files.delete(Paths.get(resource.getFolderName(), resource.getName()));
    }

    @Override
    public boolean exist(FSResource resource) {
        return Files.exists(Paths.get(resource.getFolderName(), resource.getName()));
    }

    @Override
    public boolean supports(Class<? extends Resource> clazz) {
        return FSResource.class.isAssignableFrom(clazz);
    }
}
