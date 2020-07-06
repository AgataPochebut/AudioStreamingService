package com.epam.songservice.jms;

import com.epam.songservice.model.Song;
import com.epam.storageservice.model.Resource;
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

//    public void upload(Resource source, String name) {
//
//        jmsTemplate.send("upload", new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                try {
//                    BytesMessage bytesMessage = session.createBytesMessage();
//                    InputStream fileInputStream = source.getInputStream();
//                    final int BUFLEN = 64;
//                    byte[] buf1 = new byte[BUFLEN];
//                    int bytes_read = 0;
//                    while ((bytes_read = fileInputStream.read(buf1)) != -1) {
//                        bytesMessage.writeBytes(buf1, 0, bytes_read);
//                    }
//                    fileInputStream.close();
//
//                    bytesMessage.setStringProperty("name", name);
//
//                    bytesMessage.setJMSCorrelationID(RandomStringUtils.randomAscii(24));
//                    bytesMessage.setJMSMessageID("1");
//
//                    return bytesMessage;
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        });
//    }
//
//    public void download(Long id) {
//
//        jmsTemplate.send("download", new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                try {
//                    Message bytesMessage = session.createMessage();
//
//                    bytesMessage.setLongProperty("id", id);
//
//                    bytesMessage.setJMSCorrelationID(RandomStringUtils.randomAscii(24));
//
//                    return bytesMessage;
//                } catch (Exception e) {
//                    return null;
//                }
//            }
//        });
//    }

    public List<Song> upload(Resource resource, String queue) throws JMSException {

        ObjectMessage receiveMessage = (ObjectMessage) jmsTemplate.sendAndReceive(queue, new MessageCreator() {
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

        return (List<Song>)receiveMessage.getObject();
    }

    public Resource convert(Resource resource, String queue) throws JMSException {

        ObjectMessage receiveMessage = (ObjectMessage) jmsTemplate.sendAndReceive(queue, new MessageCreator() {
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

        return (Resource)receiveMessage.getObject();
    }
}
