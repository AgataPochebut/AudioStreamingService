package com.epam.songservice.jms;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.InputStream;

@Component
@Slf4j
public class Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(Resource source, String name) {

        jmsTemplate.send("storage", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    BytesMessage bytesMessage = session.createBytesMessage();
                    InputStream fileInputStream = source.getInputStream();
                    final int BUFLEN = 64;
                    byte[] buf1 = new byte[BUFLEN];
                    int bytes_read = 0;
                    while ((bytes_read = fileInputStream.read(buf1)) != -1) {
                        bytesMessage.writeBytes(buf1, 0, bytes_read);
                    }
                    fileInputStream.close();

                    bytesMessage.setStringProperty("name", name);

                    bytesMessage.setJMSCorrelationID(RandomStringUtils.randomAscii(24));
                    bytesMessage.setJMSMessageID("1");

                    return bytesMessage;
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }

}
