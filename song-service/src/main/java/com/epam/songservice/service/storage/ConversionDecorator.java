package com.epam.songservice.service.storage;

import com.epam.songservice.model.Resource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.client.RestTemplate;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.InputStream;

public class ConversionDecorator extends ResourceStorageDecorator {

    private RestTemplate restTemplate;

    private JmsTemplate jmsTemplate;

    public ConversionDecorator(ResourceStorageService storageService, RestTemplate restTemplate) {
        super(storageService);
        this.restTemplate = restTemplate;
    }

    public ConversionDecorator(ResourceStorageService storageService, JmsTemplate jmsTemplate) {
        super(storageService);
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public Resource upload(final org.springframework.core.io.Resource source, String name) throws Exception {
        String format = "mp3";

        if (!FilenameUtils.getExtension(source.getFilename()).equals(format)) {

            //sync mq
            BytesMessage message = (BytesMessage) jmsTemplate.sendAndReceive("conversion", new MessageCreator() {
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
                        bytesMessage.setStringProperty("format", format);

                        bytesMessage.setJMSCorrelationID("123");

                        return bytesMessage;
                    } catch (Exception e) {
                        return null;
                    }

                }
            });

            byte[] data = new byte[(int) message.getBodyLength()];
            message.readBytes(data);

            org.springframework.core.io.Resource newsource = new ByteArrayResource(data);
            String newname = message.getStringProperty("name");

            return super.upload(newsource, newname);
        }
        return super.upload(source, name);
    }

    @Override
    public String test() {
        return super.test() + " Conversion";
    }

}
