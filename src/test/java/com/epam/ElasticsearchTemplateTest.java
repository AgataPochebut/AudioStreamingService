package com.epam;

import com.epam.model.Song;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

@SpringBootTest
public class ElasticsearchTemplateTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Test
    void createIndex() {
        elasticsearchTemplate.createIndex(Song.class);
    }

    @Test
    void deleteIndex() {
        elasticsearchTemplate.deleteIndex(Song.class);
    }

    @Test
    void search(){
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .build();

        elasticsearchTemplate.queryForList(searchQuery, Song.class);
    }

}
