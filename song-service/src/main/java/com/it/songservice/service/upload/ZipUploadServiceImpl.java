package com.it.songservice.service.upload;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadStatus;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import javax.xml.bind.DatatypeConverter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class ZipUploadServiceImpl implements ZipUploadService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    ResourceUploadService resourceUploadService;

    @Autowired
    ResourceStorageServiceManager resourceStorageServiceManager;

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
                        resourceUploadService.upload(resource1);//or uploadResource

                        resourceUploadService.setCorr(resource, resource1);
                    } catch (Exception e) {
                        String errorMessage = e.getMessage();
                        log.error(errorMessage, e);

                        resourceUploadService.setMess(resource, errorMessage);
                    }
                }
                zin.closeEntry();
            }
            zin.close();

            resourceUploadService.setStatus(resource, UploadStatus.FINISHED);
        }
        catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);

            resourceUploadService.setMess(resource, errorMessage);
            resourceUploadService.setStatus(resource, UploadStatus.FAILED);

            throw new UploadException("Can't unzip archive " + resource.getName(), e);
        }
        finally {
            try {
                resourceStorageServiceManager.delete(resource);
            } catch (Exception ex) {

            }
        }
    }

    private void uploadResource(Resource resource) {
        jmsTemplate.convertAndSend("upload_resource", resource, message -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                byte[] bytes = SerializationUtils.serialize(authentication);
                String auth = DatatypeConverter.printBase64Binary(bytes);
                message.setStringProperty("authentication", auth);
            }
            return message;
        });
    }

}
