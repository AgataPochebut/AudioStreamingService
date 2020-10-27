package com.it.songservice.service.unzip;

import com.it.songservice.exception.UploadException;
import com.it.songservice.jms.Producer;
import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class ResourceUnzipServiceImpl implements ResourceUnzipService {

    @Autowired
    ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    Producer producer;

    @Override
    public void upload(Resource resource) throws Exception {
        try {
            org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);
            ZipInputStream zin = new ZipInputStream(source.getInputStream());
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    try {
                        byte[] content = IOUtils.toByteArray(zin);
                        org.springframework.core.io.Resource source1 = new ByteArrayResource(content);
                        String name1 = FilenameUtils.getName(entry.getName());
                        Resource resource1 = resourceStorageServiceManager.upload(source1, name1);
                        producer.upload(resource1);
                        // exception & del res - handle in producer + throw uplExc - handle here
                    } catch (Exception e) {
                        String errorMessage = e.getMessage();
                        log.error(errorMessage, e);
                    }
                }
                zin.closeEntry();
            }
            zin.close();
            resourceStorageServiceManager.delete(resource);
        } catch (Exception e) {
            try {
                resourceStorageServiceManager.delete(resource);
            } catch (Exception ex) {

            }
            throw new UploadException("Unzip exc in " + resource.getName(), e);
        }
    }
}
