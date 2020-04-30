package com.epam.indexservice.jms;

import com.epam.indexservice.model.Song;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @JmsListener(destination = "index.in")
    public void listenIndex(ObjectMessage message) throws JMSException, ClassNotFoundException {
        elasticsearchTemplate.putMapping(Class.forName(message.getStringProperty("class")), message.getObject());
    }

    @JmsListener(destination = "index.create.song")
    public void listenIndex(Song message) throws JMSException, ClassNotFoundException {
        elasticsearchTemplate.putMapping(Song.class, message);
    }

    @JmsListener(destination = "index.delete.song")
    public void listenDelete(Song message){
        elasticsearchTemplate.delete(Song.class, message.getId().toString());
    }

//    @JmsListener(destination = "index.search.song")
//    public void listenDelete(String message){
//
//        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
//
//        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(queryBuilder)
//                .build();
//
//        (List<T>) elasticsearchTemplate.queryForList(searchQuery, Song.class);
//    }
}
