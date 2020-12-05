package com.it.searchservice.service;

import com.it.searchservice.repository.GenericRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.List;

public class GenericRepositoryServiceImpl<T,U> implements GenericRepositoryService<T, U> {

    @Autowired
    private GenericRepository<T,U> repository;

    @Override
    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        repository.findAll().forEach(i -> list.add(i));
        return list;
    }

    @Override
    public T findById(U id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    @Override
    public T save(T entity) throws Exception {
        return repository.save(entity);
    }

    @Override
    public T update(T entity) throws Exception {
        return repository.save(entity);
    }

    @Override
    public void deleteById(U id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);}
    }

    @Override
    public List<T> search(String keyword){
        QueryBuilder queryBuilder = (keyword == null)
                ? QueryBuilders.matchAllQuery()
                : QueryBuilders.queryStringQuery(keyword);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("songs")
                .withQuery(queryBuilder)
                .build();

        List<T> list = new ArrayList<>();
        repository.search(searchQuery).forEach(i -> list.add(i));
        return list;
    }
}
