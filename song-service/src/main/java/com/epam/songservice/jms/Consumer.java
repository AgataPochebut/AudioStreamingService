package com.epam.songservice.jms;

import com.epam.songservice.model.Resource;
import com.epam.songservice.model.Song;
import com.epam.songservice.service.storage.resource.ResourceStorageServiceManager;
import com.epam.songservice.service.storage.song.SongStorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @Autowired
    private SongStorageService songStorageService;

    @JmsListener(destination = "upl")
    @Async
    public void upload(ObjectMessage message) throws Exception {
        Resource resource = (Resource) message.getObject();
        List<Song> result = uploadZip(resource);

        jmsTemplate.send(message.getJMSReplyTo(), new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    ObjectMessage sendMessage = session.createObjectMessage();
                    sendMessage.setObject((Serializable) result);
                    return sendMessage;
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }

    public List<Song> uploadZip(Resource resource) throws Exception {
        final List<Song> entity = new ArrayList<>();

        org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);
        String name = resource.getName();
        ZipInputStream zin = new ZipInputStream(source.getInputStream());
        ZipEntry entry;
        while ((entry = zin.getNextEntry()) != null) {
            if (!entry.isDirectory()) {
                try {
                    byte[] content = IOUtils.toByteArray(zin);
                    org.springframework.core.io.Resource source1 = new ByteArrayResource(content);
                    String name1 = entry.getName();
                    if (FilenameUtils.getExtension(name1).equals("zip")) {
                        entity.addAll(songStorageService.uploadZip(source1, name1));
                    } else {
                        entity.add(songStorageService.upload(source1, name1));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
            }
            zin.closeEntry();
        }
        zin.close();

        return entity;
    }

}
