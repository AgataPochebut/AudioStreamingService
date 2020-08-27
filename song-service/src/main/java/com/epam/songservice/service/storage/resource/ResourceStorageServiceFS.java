package com.epam.songservice.service.storage.resource;

import com.epam.songservice.annotation.Decorate;
import com.epam.songservice.annotation.StorageType;
import com.epam.songservice.model.FSResource;
import com.epam.songservice.model.Resource;
import com.epam.songservice.model.StorageTypes;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import java.io.*;


@Decorate(ResourceStorageDecorator.class)
@StorageType(StorageTypes.FS)
@Service
public class ResourceStorageServiceFS implements ResourceStorageService {

    @Value("${fs.defaultFolder}")
    private String defaultBaseFolder;

    @Override
    public Resource upload(org.springframework.core.io.Resource source, String name) throws IOException {
        int count = 0;
        File file = new File(defaultBaseFolder, name);
        while (file.exists()){
            count++;
            file = new File(defaultBaseFolder, FilenameUtils.removeExtension(name) + " ("+ count + ")" + "." + FilenameUtils.getExtension(name));
        }
        file.getParentFile().mkdirs();

        OutputStream output = new FileOutputStream(file);
        IOUtils.copy(source.getInputStream(), output);
        output.close();

        FSResource resource = new FSResource();
        resource.setName(file.getName());
        resource.setSize(source.contentLength());
        resource.setChecksum(DigestUtils.md5Hex(source.getInputStream()));
        resource.setFolderName(file.getParentFile().getAbsolutePath());
        return resource;
    }

    @Override
    public org.springframework.core.io.Resource download(Resource resource) throws IOException {
        FSResource resource1 = (FSResource)resource;
        org.springframework.core.io.Resource source = new FileSystemResource(new File(resource1.getFolderName(), resource1.getName()));
        InputStream in = source.getInputStream();
        byte[] content = IOUtils.toByteArray(in);
        in.close();
        return new ByteArrayResource(content);
    }

    @Override
    public void delete(Resource resource) throws IOException {
        FSResource resource1 = (FSResource)resource;
        new File(resource1.getFolderName(), resource1.getName()).delete();
    }

    @Override
    public boolean exist(Resource resource) {
        FSResource resource1 = (FSResource)resource;
        return new File(resource1.getFolderName(), resource1.getName()).exists();
    }

    @Override
    public String test() {
        return "FS";
    }

}
