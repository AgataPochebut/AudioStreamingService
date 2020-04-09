package com.epam.service.search;

import com.epam.es_repository.GenericElasticsearchRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericSearchServiceImpl<T,U> implements GenericSearchService<T, U> {

    @Autowired
    private GenericElasticsearchRepository<T,U> repository;

    @Override
    public Iterable<T> findAll() {
        return repository.findAll();
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
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);}
    }

    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }
}
