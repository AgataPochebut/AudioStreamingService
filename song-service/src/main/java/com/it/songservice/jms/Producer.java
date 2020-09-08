package com.it.songservice.jms;

import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;
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
import java.util.List;

@Component
@Slf4j
public class Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public List<Song> upload(Resource resource) throws JMSException {
        ObjectMessage receiveMessage = (ObjectMessage) jmsTemplate.sendAndReceive("upl", new MessageCreator() {
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
        List<Song> result = (List<Song>)receiveMessage.getObject();
        return result;
    }
}
