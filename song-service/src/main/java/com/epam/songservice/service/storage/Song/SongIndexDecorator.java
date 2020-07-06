package com.epam.songservice.service.storage.Song;

import com.epam.songservice.feign.index.SongIndexClient;
import com.epam.songservice.model.Song;
import com.epam.storageservice.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class SongIndexDecorator extends SongStorageDecorator {

    @Autowired
    private SongIndexClient indexClient;

    @Autowired
    private JmsTemplate jmsTemplate;

    public SongIndexDecorator(SongStorageService storageService, SongIndexClient indexService) {
        super(storageService);
        this.indexClient = indexService;
    }

    public SongIndexDecorator(SongStorageService storageService, JmsTemplate jmsTemplate) {
        super(storageService);
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public Song upload1(Resource resource) throws Exception {
        Song entity = super.upload1(resource);
        indexClient.create(entity);
        return entity;
    }

    @Override
    public void delete(Song entity) {
        super.delete(entity);

//        indexService.delete(entity);
        //or
//        indexClient.delete(entity.getId());

        //or

        //sync mq
        jmsTemplate.sendAndReceive("index.delete", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    Message message = session.createMessage();

                    message.setObjectProperty("type", "songs");
                    message.setObjectProperty("id", entity.getId());

                    message.setJMSCorrelationID("123");
                    return message;
                } catch (Exception e) {
                    return null;
                }
            }
        });
    }

    @Override
    public String test() {
        return super.test() + " Index";
    }

}
