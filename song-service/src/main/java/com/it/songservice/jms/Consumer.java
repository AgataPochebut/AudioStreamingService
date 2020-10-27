package com.it.songservice.jms;

import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.song.SongStorageService;
import com.it.songservice.service.unzip.ResourceUnzipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.jms.ObjectMessage;
import javax.xml.bind.DatatypeConverter;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private SongStorageService songStorageService;

    @Autowired
    private ResourceUnzipService resourceUnzipService;

    @JmsListener(destination = "upload_zip")
    public void uploadResource(ObjectMessage message) throws Exception {
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Resource resource = (Resource) message.getObject();
        try {
            resourceUnzipService.upload(resource);
            // exception & del res - handle in resourceUnzipService + throw uplExc - handle here
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);
        }
    }

    @JmsListener(destination = "upload_audio")
    public void uploadAudio(ObjectMessage message) throws Exception {
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Resource resource = (Resource) message.getObject();
        try {
            songStorageService.upload(resource);
            // exception & del res - handle in songStServ + throw uplExc - handle here
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);
        }
    }
}