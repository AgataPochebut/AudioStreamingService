package com.it.searchservice.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;

@Component
@NoRepositoryBean
public interface GenericRepository<T,U> extends ElasticsearchRepository<T,U> {
}
