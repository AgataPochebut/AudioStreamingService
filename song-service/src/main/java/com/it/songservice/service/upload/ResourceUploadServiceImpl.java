package com.it.songservice.service.upload;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadResult;
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

import javax.jms.*;
import javax.xml.bind.DatatypeConverter;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceUploadServiceImpl implements ResourceUploadService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Override
    public void upload(Resource resource) throws Exception {
        setStatus(resource, UploadStatus.PROCEEDED);

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

            setMess(resource, errorMessage);
            setStatus(resource, UploadStatus.FAILED);

            throw new UploadException("Can't upload file " + resource.getName(), e);
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


    @Override
    public UploadResult getResultById(Long id) {
        UploadResult uploadResult = new UploadResult();
        uploadResult.setId(id);
        uploadResult.setMessages(getMess(id));

        Set<Long> corr = getCorr(id);
        if (corr!= null && !corr.isEmpty()) {
            Set<UploadResult> details = corr.stream()
                    .map(i -> getResultById(i))
                    .collect(Collectors.toSet());

            uploadResult.setDetails(details);

            UploadStatus status = details.stream()
                    .map(i -> i.getStatus())
                    .min(Comparator.comparingInt(UploadStatus::getValue)).get();

            uploadResult.setStatus(status);
        } else {
            uploadResult.setStatus(getStatus(id));
        }

        return uploadResult;
    }

    public void setStatus(Resource resource, UploadStatus status) {
        jmsTemplate.execute((session, producer) -> {
            Destination dest = session.createQueue("status");

            Message message = session.createObjectMessage(status);
            message.setJMSCorrelationID(String.valueOf(resource.getId()));

            producer.send(dest, message);
            return null;
        });
    }

    public void setMess(Resource resource, String mess) {
        jmsTemplate.execute((session, producer) -> {
            Destination dest = session.createQueue("mess");

            Message message = session.createTextMessage(mess);
//            Message message = session.createObjectMessage(e);
            message.setJMSCorrelationID(String.valueOf(resource.getId()));

            producer.send(dest, message);
            return null;
        });
    }

    public void setCorr(Resource resource, Resource resource1) {
        jmsTemplate.execute((session, producer) -> {
            Destination dest = session.createQueue("corr");

            Message message = session.createTextMessage(String.valueOf(resource1.getId()));
            message.setJMSCorrelationID(String.valueOf(resource.getId()));

            producer.send(dest, message);
            return null;
        });
    }

    public UploadStatus getStatus(Long id) {
        String selector = String.format("JMSCorrelationID='%s'", id);

        return jmsTemplate.execute((session) -> {
            UploadStatus result = null;
            Destination destination = session.createQueue("status");
            QueueBrowser browser = session.createBrowser((Queue) destination, selector);
            Enumeration e = browser.getEnumeration();
            Message message;
            while ((message = (Message) e.nextElement()) != null) {
                if (message instanceof TextMessage) {
                    result = UploadStatus.valueOf(((TextMessage) message).getText());
                } else if (message instanceof ObjectMessage) {
                    result = (UploadStatus) ((ObjectMessage) message).getObject();
                }
            }
            browser.close();
            return result;
        }, true);
    }

    public Set<String> getMess(Long id) {
        String selector = String.format("JMSCorrelationID='%s'", id);

        return jmsTemplate.execute((session) -> {
            Set<String> result = new HashSet<>();
            Destination destination = session.createQueue("mess");
            QueueBrowser browser = session.createBrowser((Queue) destination, selector);
            Enumeration e = browser.getEnumeration();
            Message message;
            while ((message = (Message) e.nextElement()) != null) {
                if (message instanceof TextMessage) {
                    result.add(((TextMessage) message).getText());
                } else if (message instanceof ObjectMessage) {
                    result.add((String) ((ObjectMessage) message).getObject());
                }
            }
            browser.close();
            return result;
        }, true);
    }

    public Set<Long> getCorr(Long id) {
        String selector = String.format("JMSCorrelationID='%s'", id);

        return jmsTemplate.execute((session) -> {
            Set<Long> result = new HashSet<>();
            Destination destination = session.createQueue("corr");
            QueueBrowser browser = session.createBrowser((Queue) destination, selector);
            Enumeration e = browser.getEnumeration();
            Message message;
            while ((message = (Message) e.nextElement()) != null) {
                if (message instanceof TextMessage) {
                    result.add(Long.valueOf(((TextMessage) message).getText()));
                } else if (message instanceof ObjectMessage) {
                    result.add((Long) ((ObjectMessage) message).getObject());
                }
            }
            browser.close();
            return result;
        }, true);
    }

}
