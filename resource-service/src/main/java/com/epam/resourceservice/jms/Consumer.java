package com.epam.resourceservice.jms;

import com.epam.resourceservice.service.storage.ResourceStorageService;
import com.epam.storageservice.model.Resource;
import com.epam.storageservice.service.ResourceStorageFactory;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Mapper mapper;

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    @Autowired
    private ResourceStorageService resourceStorageService;

    @JmsListener(destination = "stor")
    public void upload(ObjectMessage message) throws Exception {

        Resource resource = (Resource) message.getObject();

        org.springframework.core.io.Resource source = resourceStorageFactory.getService().download(resource);
        String name = resource.getName();

        resourceStorageService.upload()
        Resource resource1 = resourceStorageFactory.getService().upload(source, name);

        resourceStorageFactory.getService().delete(resource);

        jmsTemplate.send(message.getJMSReplyTo(), new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    ObjectMessage sendMessage = session.createObjectMessage();
                    sendMessage.setObject(resource1);

                    return sendMessage;
                } catch (Exception e) {
                    return null;
                }
            }
        });

    }
}
