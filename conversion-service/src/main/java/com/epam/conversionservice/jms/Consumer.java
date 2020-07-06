package com.epam.conversionservice.jms;

import com.epam.conversionservice.service.ConversionService;
import com.epam.storageservice.model.Resource;
import com.epam.storageservice.service.ResourceStorageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.jms.*;
import java.io.*;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ResourceStorageFactory resourceStorageFactory;

    @Autowired
    private ConversionService conversionService;

    @Value("${conversion.defaultFolder}")
    private String defaultBaseFolder;

    @JmsListener(destination = "conv")
    public void upload(ObjectMessage message) throws Exception {
        //json
        Resource resource = (Resource) message.getObject();
        String format = message.getStringProperty("format");
        //io
        org.springframework.core.io.Resource source = resourceStorageFactory.getService().download(resource);
        String name = resource.getName();
        //temp in fs
        File file = new File(defaultBaseFolder, name);
        file.getParentFile().mkdirs();
        FileCopyUtils.copy(source.getInputStream(), new FileOutputStream(file));
        //conv
        File file1 = conversionService.convert(file, format);
        //io
        org.springframework.core.io.Resource source1 = new FileSystemResource(file1);
        String name1 = file1.getName();
        //json
        Resource resource1 = resourceStorageFactory.getService().upload(source1, name1);

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
