package com.epam.storageservice.service.repository;

import com.epam.storageservice.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

public class GenericServiceImpl<T,U> implements GenericService<T, U> {

    @Autowired
    private GenericRepository<T,U> repository;

//    @Cacheable(cacheNames = "cachetest")
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

//    @Cacheable(cacheNames = "cachetest")
    @Override
    public T findById(U id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException());
    }

//    @CachePut(cacheNames = "cachetest")
    @Override
    public T save(T entity) throws Exception {
        return repository.save(entity);
    }

//    @CachePut(cacheNames = "cachetest")
    @Override
    public T update(T entity) throws Exception {
        return repository.save(entity);
    }

//    @CacheEvict(cacheNames = "cachetest")
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
