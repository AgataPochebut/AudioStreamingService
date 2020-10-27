package com.it.songservice.jms;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.service.storage.resource.ResourceStorageServiceManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.xml.bind.DatatypeConverter;

@Component
@Slf4j
public class Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    public void upload(Resource resource) throws Exception {
        try {
            String name = resource.getName();
            if (FilenameUtils.getExtension(name).equals("zip")) {
                uploadZip(resource);
            } else if (FilenameUtils.getExtension(name).equalsIgnoreCase("mp3") ||
                    FilenameUtils.getExtension(name).equalsIgnoreCase("wav")) {
                uploadAudio(resource);
            } else {
                throw new UploadException("Format exc in " + resource.getName());
            }
        } catch (Exception e) {
            try {
                resourceStorageServiceManager.delete(resource);
            } catch (Exception ex) {

            }
            throw new UploadException("Producer exc in " + resource.getName(), e);
        }
    }

    public void uploadZip(Resource resource) throws Exception {
        jmsTemplate.send("upload_zip", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage sendMessage = session.createObjectMessage();
                sendMessage.setObject(resource);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null) {
                    byte[] bytes = SerializationUtils.serialize(authentication);
                    String auth = DatatypeConverter.printBase64Binary(bytes);
                    sendMessage.setStringProperty("authentication", auth);
                }

                sendMessage.setJMSCorrelationID(RandomStringUtils.randomAscii(24));
                return sendMessage;
            }
        });
    }

    public void uploadAudio(Resource resource) throws Exception {
        jmsTemplate.send("upload_audio", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage sendMessage = session.createObjectMessage();
                sendMessage.setObject(resource);

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null) {
                    byte[] bytes = SerializationUtils.serialize(authentication);
                    String auth = DatatypeConverter.printBase64Binary(bytes);
                    sendMessage.setStringProperty("authentication", auth);
                }

                sendMessage.setJMSCorrelationID(RandomStringUtils.randomAscii(24));
                return sendMessage;
            }
        });
    }

}
