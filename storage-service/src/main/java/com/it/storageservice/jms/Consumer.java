package com.it.storageservice.jms;

import com.it.storageservice.model.Resource;
import com.it.storageservice.service.storage.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.jms.ObjectMessage;
import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private Producer producer;

    @JmsListener(destination = "upload_zip")
    public void upload(ObjectMessage message) throws Exception {
        // TODO: 21.10.2020  
        String auth = message.getStringProperty("authentication");
        byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
        Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Resource resource = (Resource) message.getObject();
        uploadZip(resource);
    }

    public List<Resource> uploadZip(Resource resource) throws Exception {
        final List<Resource> list = new ArrayList<>();

        try {
            org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);
            ZipInputStream zin = new ZipInputStream(source.getInputStream());
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    byte[] content = IOUtils.toByteArray(zin);
                    org.springframework.core.io.Resource source1 = new ByteArrayResource(content);
                    String name1 = FilenameUtils.getName(entry.getName());
                    if (FilenameUtils.getExtension(name1).equals("zip")) {
                        Resource resource1 = resourceStorageServiceManager.upload(source1, name1);
                        list.addAll(uploadZip(resource1));
                    } else {
                        Resource resource1 = resourceStorageServiceManager.upload(source1, name1);
                        list.add(resource1);
                        producer.upload(resource1);
                    }
                }
                zin.closeEntry();
            }
            zin.close();
        } catch (Exception e) {
            for (Resource i : list) {
                resourceStorageServiceManager.delete(i);
            }
            list.clear();

            String errorMessage = e.getMessage();
            log.error(errorMessage, e);
        } finally {
            resourceStorageServiceManager.delete(resource);
        }

        return list;
    }

}