package com.it.songservice.service.upload;

import com.it.songservice.model.Resource;
import com.it.songservice.model.UploadResult;
import com.it.songservice.model.UploadStatus;
import com.it.songservice.service.repository.UploadResultRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.jms.Message;

@Transactional
@Service
@Slf4j
public class UploadResultServiceImpl implements UploadResultService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private UploadResultRepositoryService repositoryService;

    public void setStatus(Resource resource, UploadStatus status) {
       setStatus(repositoryService.findByResource(resource.getId()), status);
    }

    public void setStatus(UploadResult result, UploadStatus status) {
        jmsTemplate.execute((session, producer) -> {
            Destination dest = session.createQueue("status");

            Message message = session.createObjectMessage(status);
            message.setJMSCorrelationID(String.valueOf(result.getId()));

            producer.send(dest, message);
            return null;
        });
    }

    public void setMess(Resource resource, String mess) {
        setMess(repositoryService.findByResource(resource.getId()), mess);
    }

    public void setMess(UploadResult result, String mess) {
        jmsTemplate.execute((session, producer) -> {
            Destination dest = session.createQueue("mess");

            Message message = session.createTextMessage(mess);
            message.setJMSCorrelationID(String.valueOf(result.getId()));

            producer.send(dest, message);
            return null;
        });
    }

    public void receiveStatus(Long upload_id, UploadStatus status) throws Exception {

        UploadResult result = repositoryService.findById(upload_id);
        result.setStatus(status);
        result = repositoryService.save(result);

        countStatus(result);
        if (result.getParent() != null) {
            countStatus(result.getParent());
        }
    }

    void countStatus(UploadResult result) {
        if (result.getStatus() == UploadStatus.UNPACKED &&
                result.getDetails().stream()
                        .map(i -> i.getStatus())
                        .allMatch(i -> (i == UploadStatus.FINISHED || i == UploadStatus.FAILED))) {
            setStatus(result, UploadStatus.FINISHED);
        }
    }

    public void receiveMess(Long upload_id, String mess) throws Exception {
        log.error(mess);
    }
}
