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
    private SongStorageService songStorageService;

    @Autowired
    private ResourceStorageServiceManager resourceStorageServiceManager;

    @JmsListener(destination = "upl")
    public void upload(ObjectMessage message) throws Exception {
        Resource resource = (Resource) message.getObject();
        List<Song> result = uploadRecursively(resource);

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

    public List<Song> uploadRecursively(Resource resource) throws Exception {

        final List<Song> entity = new ArrayList<>();

        if (FilenameUtils.getExtension(resource.getName()).equals("zip")) {
            org.springframework.core.io.Resource source = resourceStorageServiceManager.download(resource);

            ZipInputStream zin = new ZipInputStream(source.getInputStream());
            ZipEntry entry;
            while ((entry = zin.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    //чтобы загрузились остальные песни
                    try {
                        byte[] content = IOUtils.toByteArray(zin);
                        org.springframework.core.io.Resource source1 = new ByteArrayResource(content);
                        String name1 = entry.getName();
                        Resource resource1 = resourceStorageServiceManager.upload(source1, name1);
                        List<Song> entity1 = uploadRecursively(resource1);
                        entity.addAll(entity1);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        continue;
                    }
                }
                zin.closeEntry();
            }
            zin.close();

            resourceStorageServiceManager.delete(resource);
        } else {
            Song entity1 = songStorageService.upload(resource);
            entity.add(entity1);
        }

        return entity;
    }
}
