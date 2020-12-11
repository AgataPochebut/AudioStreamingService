package com.it.songservice.service.upload;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadStatus;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import javax.xml.bind.DatatypeConverter;

@Service
@Slf4j
public class ResourceUploadServiceImpl implements ResourceUploadService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private UploadResultService uploadResultService;

    @Override
    public void upload(Resource resource) throws Exception {
        try {
            String name = resource.getName();
            if (FilenameUtils.getExtension(name).equals("zip")) {
                uploadZip(resource);
            } else if (FilenameUtils.getExtension(name).equalsIgnoreCase("mp3") ||
                    FilenameUtils.getExtension(name).equalsIgnoreCase("wav")) {
                uploadSong(resource);
            } else {
                throw new UploadException("Format exc in " + resource.getName());
            }
        } catch (Exception e) {
            try {
                resourceStorageServiceManager.delete(resource);
            } catch (Exception ex) {

            }

            String errorMessage = e.getMessage();
            log.error(errorMessage, e);

            uploadResultService.setStatus(resource, UploadStatus.FAILED);
            uploadResultService.setMess(resource, errorMessage);
        }
    }

    private void uploadZip(Resource resource) {
        jmsTemplate.convertAndSend("upload_zip", resource, message -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                byte[] bytes = SerializationUtils.serialize(authentication);
                String auth = DatatypeConverter.printBase64Binary(bytes);
                message.setStringProperty("authentication", auth);
            }
            return message;
        });
    }

    private void uploadSong(Resource resource) {
        jmsTemplate.convertAndSend("upload_song", resource, message -> {
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
