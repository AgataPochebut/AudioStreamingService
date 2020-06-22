package com.epam.searchservice.jms;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;

@Component
@Slf4j
public class Consumer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private RestHighLevelClient elasticsearchClient;

    @JmsListener(destination = "index.create")
    public void create(Message message) throws IOException, JMSException {
        IndexRequest request = new IndexRequest();
        request.index("service");
        request.type(message.getStringProperty("type"));
        request.id(message.getStringProperty("id"));
        request.source(message.getObjectProperty("source"), XContentType.JSON);
        elasticsearchClient.index(request, RequestOptions.DEFAULT);
    }

    @JmsListener(destination = "index.delete")
    public void delete(Message message) throws IOException, JMSException {
        DeleteRequest request = new DeleteRequest();
        request.index("service");
        request.type(message.getStringProperty("type"));
        request.id(message.getStringProperty("id"));
        elasticsearchClient.delete(request, RequestOptions.DEFAULT);
    }

}
