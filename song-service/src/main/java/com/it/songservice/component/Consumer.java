package com.it.songservice.component;

import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadStatus;
import com.it.songservice.service.upload.ResourceUploadService;
import com.it.songservice.service.upload.SongUploadService;
import com.it.songservice.service.upload.UploadResultService;
import com.it.songservice.service.upload.ZipUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.xml.bind.DatatypeConverter;

@Profile("prod")
@Component
@Slf4j
public class Consumer {

    @Autowired
    private ResourceUploadService resourceUploadService;

    @Autowired
    private ZipUploadService zipUploadService;

    @Autowired
    private SongUploadService songUploadService;

    @Autowired
    private UploadResultService uploadResultService;

    @JmsListener(destination = "upload_resource")
    public void uploadResource(ObjectMessage message) throws Exception {
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Resource resource = (Resource) message.getObject();
        try {
            resourceUploadService.upload(resource);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);
        }
    }

    @JmsListener(destination = "upload_zip")
    public void uploadZip(ObjectMessage message) throws Exception {
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Resource resource = (Resource) message.getObject();
        try {
            zipUploadService.upload(resource);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);
        }
    }

    @JmsListener(destination = "upload_song")
    public void uploadSong(ObjectMessage message) throws Exception {
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Resource resource = (Resource) message.getObject();
        try {
            songUploadService.upload(resource);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);
        }
    }

    @JmsListener(destination = "status")
    public void status(ObjectMessage message) throws Exception {
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Long resource_id = Long.valueOf(message.getJMSCorrelationID());
        UploadStatus status = (UploadStatus) message.getObject();
        uploadResultService.receiveStatus(resource_id, status);
    }

    @JmsListener(destination = "mess")
    public void status(TextMessage message) throws Exception {
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Long resource_id = Long.valueOf(message.getJMSCorrelationID());
        String mess = message.getText();
        uploadResultService.receiveMess(resource_id, mess);
    }

}