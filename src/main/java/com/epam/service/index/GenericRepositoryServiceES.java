package com.epam.service.index;

import com.epam.es_repository.GenericESRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.ArrayList;
import java.util.List;

public class GenericRepositoryServiceES<T,U> implements GenericRepositoryService<T, U> {

    @Autowired
    private GenericESRepository<T,U> repository;

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

//    @Override
//    public void deleteById(U id) {
//        if (repository.findById(id).isPresent()) {
//            repository.deleteById(id);}
//    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public List<T> search(String keyword){

        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(keyword);

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
                // .withSort(new FieldSortBuilder("createTime").order(SortOrder.DESC))
                .build();

        List<T> list = new ArrayList<>();
        repository.search(searchQuery).forEach(i -> list.add(i));
        return list;
    }
}
