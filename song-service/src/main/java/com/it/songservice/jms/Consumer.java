package com.it.songservice.jms;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.it.songservice.service.storage.song.SongStorageService;
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

    @Autowired
    private SongStorageService songStorageService;

    @JmsListener(destination = "upload_resource")
    public void uploadResource(ObjectMessage message) throws Exception {
        // TODO: 21.10.2020  
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Resource resource = (Resource) message.getObject();
        upload_resource(resource);
    }

    public void upload_resource(Resource resource) throws Exception {
        try {
            String name = resource.getName();
            if (FilenameUtils.getExtension(name).equals("zip")) {
                org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);
                ZipInputStream zin = new ZipInputStream(source.getInputStream());
                ZipEntry entry;
                while ((entry = zin.getNextEntry()) != null) {
                    if (!entry.isDirectory()) {
                        byte[] content = IOUtils.toByteArray(zin);
                        org.springframework.core.io.Resource source1 = new ByteArrayResource(content);
                        String name1 = FilenameUtils.getName(entry.getName());
                        Resource resource1 = resourceStorageServiceManager.upload(source1, name1);
                        producer.uploadResource(resource1);
                    }
                    zin.closeEntry();
                }
                zin.close();
                resourceStorageServiceManager.delete(resource);
            } else if (FilenameUtils.getExtension(name).equalsIgnoreCase("mp3") ||
                    FilenameUtils.getExtension(name).equalsIgnoreCase("wav")) {
                producer.uploadAudio(resource);
            } else throw new UploadException("Format");
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);
            resourceStorageServiceManager.delete(resource);
        }
    }

    @JmsListener(destination = "upload_audio")
    public void uploadAudio(ObjectMessage message) throws Exception {
        // TODO: 21.10.2020
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Resource resource = (Resource) message.getObject();
        upload_audio(resource);
    }

    public void upload_audio(Resource resource) throws Exception {
        try {
            songStorageService.upload(resource);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);
            try {
                resourceStorageServiceManager.delete(resource);
            } catch (Exception ex) {

            }
        }
    }

}