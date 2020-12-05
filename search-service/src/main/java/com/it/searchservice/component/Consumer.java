package com.it.searchservice.component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.searchservice.model.Song;
import com.it.searchservice.service.SongRepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
import javax.xml.bind.DatatypeConverter;

@Profile("prod")
@Component
@Slf4j
public class Consumer {

    @Autowired
    private SongRepositoryService repositoryService;

    @JmsListener(destination = "create_index_song")
    public void uploadSong(TextMessage message) throws Exception {
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Song entity = mapper.readValue(message.getText(), Song.class);
        try {
            repositoryService.save(entity);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);
        }
    }

    @JmsListener(destination = "delete_index_song")
    public void deleteSong(ObjectMessage message) throws Exception {
        String auth = message.getStringProperty("authentication");
        if (auth != null) {
            byte[] bytes = DatatypeConverter.parseBase64Binary(auth);
            Authentication authentication = (Authentication) SerializationUtils.deserialize(bytes);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        Long id = (Long) message.getObject();
        try {
            repositoryService.deleteById(id);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            log.error(errorMessage, e);
        }
    }
}