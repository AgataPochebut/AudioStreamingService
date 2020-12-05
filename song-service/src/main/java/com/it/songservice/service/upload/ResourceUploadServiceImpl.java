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
        setStatus(resource.getId(), UploadStatus.PROCEEDED);

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

            setMess(resource.getId(), errorMessage);
            setStatus(resource.getId(), UploadStatus.FAILED);

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
    public UploadResult getResultById(Long upload_id) {
        UploadResult uploadResult = new UploadResult();
        uploadResult.setId(upload_id);
        uploadResult.setMessages(getMess(upload_id));

        Set<Long> corr = getCorr(upload_id);
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
            uploadResult.setStatus(getStatus(upload_id));
        }

        return uploadResult;
    }

    public void setStatus(Long upload_id, UploadStatus status) {
        jmsTemplate.convertAndSend("status", status, message -> {
            message.setJMSCorrelationID(String.valueOf(upload_id));
            return message;
        });
    }

    public void setMess(Long upload_id, String mess) {
        jmsTemplate.execute((session, producer) -> {
            Destination dest = session.createQueue("mess");

            Message message = session.createTextMessage(mess);
            message.setJMSCorrelationID(String.valueOf(upload_id));

            producer.send(dest, message);
            return null;
        });
    }

    public void setCorr(Long upload_id, Long corr_upload_id) {
        jmsTemplate.execute((session, producer) -> {
            Destination dest = session.createQueue("corr");

            Message message = session.createTextMessage(String.valueOf(corr_upload_id));
            message.setJMSCorrelationID(String.valueOf(upload_id));

            producer.send(dest, message);
            return null;
        });
    }

    public UploadStatus getStatus(Long upload_id) {
        String selector = String.format("JMSCorrelationID='%s'", upload_id);

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

    public Set<String> getMess(Long upload_id) {
        String selector = String.format("JMSCorrelationID='%s'", upload_id);

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

    public Set<Long> getCorr(Long upload_id) {
        String selector = String.format("JMSCorrelationID='%s'", upload_id);

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
