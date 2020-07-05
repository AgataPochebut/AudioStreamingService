package com.epam.resourceservice.jms;

import com.epam.resourceservice.dto.response.ResourceResponseDto;
import com.epam.resourceservice.model.Resource;
import com.epam.resourceservice.service.storage.ResourceStorageFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.*;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Mapper mapper;

    @JmsListener(destination = "storage")
    public void listen(BytesMessage message) throws Exception {

        byte[] data = new byte[(int) message.getBodyLength()];
        message.readBytes(data);

        org.springframework.core.io.Resource source = new ByteArrayResource(data);
        String name = message.getStringProperty("name");

        Resource entity = resourceStorageFactory.getService().upload(source, name);

        jmsTemplate.send(message.getJMSReplyTo(), new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    TextMessage textMessage = session.createTextMessage();

                    ObjectMapper Obj = new ObjectMapper();
                    textMessage.setText(Obj.writeValueAsString(mapper.map(entity, ResourceResponseDto.class)));

                    return textMessage;
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }


}
