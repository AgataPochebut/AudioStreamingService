package com.epam.songservice.jms;

import com.epam.songservice.model.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

@Component
@Slf4j
public class Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void upload(Resource resource, String queue) throws JMSException {

        jmsTemplate.send(queue, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    ObjectMessage sendMessage = session.createObjectMessage();
                    sendMessage.setObject(resource);

                    sendMessage.setJMSCorrelationID(RandomStringUtils.randomAscii(24));
                    return sendMessage;
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }

    //это можно использовать когда conv-serv читает-пишет в бд - тогла обмениваемся json
//    public Resource convert(Resource resource, String queue) throws JMSException {
//
//        ObjectMessage receiveMessage = (ObjectMessage) jmsTemplate.sendAndReceive(queue, new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                try {
//                    ObjectMessage sendMessage = session.createObjectMessage();
//                    sendMessage.setObject(resource);
//
//                    sendMessage.setJMSCorrelationID(RandomStringUtils.randomAscii(24));
//                    return sendMessage;
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        });
//
//        return (Resource)receiveMessage.getObject();
//    }
}
