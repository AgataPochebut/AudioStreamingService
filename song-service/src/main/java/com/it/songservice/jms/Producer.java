package com.it.songservice.jms;

import com.it.songservice.exception.UploadException;
import com.it.songservice.model.Resource;
import lombok.extern.slf4j.Slf4j;
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

    public void uploadZip(Resource resource) throws Exception {
        Throwable lastException;
        try {
            jmsTemplate.send("upl", new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    ObjectMessage sendMessage = session.createObjectMessage();
                    sendMessage.setObject(resource);

                    // TODO: 21.10.2020
                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    byte[] bytes = SerializationUtils.serialize(authentication);
                    String auth = DatatypeConverter.printBase64Binary(bytes);
                    sendMessage.setStringProperty("authentication", auth);

                    sendMessage.setJMSCorrelationID(RandomStringUtils.randomAscii(24));
                    return sendMessage;
                }
            });
            return;
        } catch (Exception e) {
            lastException = e;
        }
        throw new UploadException("JMS exc in " + resource.getName(), lastException);
    }
}
