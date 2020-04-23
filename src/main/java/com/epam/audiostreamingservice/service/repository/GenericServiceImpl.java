package com.epam.audiostreamingservice.service.repository;

import com.epam.audiostreamingservice.jpa_repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public class GenericServiceImpl<T,U> implements GenericService<T, U> {

    @Autowired
    private GenericRepository<T,U> repository;

    @Cacheable(cacheNames = "cachetest")
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Cacheable(cacheNames = "cachetest")
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

    @CacheEvict(cacheNames = "cachetest")
    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

    @CacheEvict(cacheNames = "cachetest")
    @Override
    public void deleteById(U id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);}
    }

}
