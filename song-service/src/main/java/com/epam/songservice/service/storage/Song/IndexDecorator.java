package com.epam.songservice.service.storage.Song;

import com.epam.songservice.feign.index.IndexClient;
import com.epam.songservice.model.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Map;

public class IndexDecorator extends SongStorageDecorator {

    @Autowired
    private IndexClient indexService;

    @Autowired
    private JmsTemplate jmsTemplate;

    public IndexDecorator(SongStorageService storageService, IndexClient indexService) {
        super(storageService);
        this.indexService = indexService;
    }

    public IndexDecorator(SongStorageService storageService, JmsTemplate jmsTemplate) {
        super(storageService);
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public Song upload(org.springframework.core.io.Resource source, String name) throws Exception {
        Song entity = super.upload(source, name);

//        indexService.create(entity);

        //or

        //sync mq
        jmsTemplate.sendAndReceive("index.create", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                try {
                    Message message = session.createMessage();

                    message.setObjectProperty("type", "songs");
                    message.setObjectProperty("id", entity.getId());

                    ObjectMapper Obj = new ObjectMapper();
//                    message.setObjectProperty("source", Obj.writeValueAsString(entity));
                    message.setObjectProperty("source", Obj.convertValue(entity, Map.class));

                    message.setJMSCorrelationID("123");
                    return message;
                } catch (Exception e) {
                    return null;
                }
            }
        });

        return entity;
    }

    @Override
    public void delete(Song entity) {
        super.delete(entity);

//        indexService.delete(entity);

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
        return super.test() + " DBInsert";
    }

}
