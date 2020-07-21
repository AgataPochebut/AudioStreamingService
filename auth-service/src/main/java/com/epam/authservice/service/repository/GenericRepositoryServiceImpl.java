package com.epam.authservice.service.repository;

import com.epam.authservice.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GenericRepositoryServiceImpl<T,U> implements GenericRepositoryService<T, U> {

    @Autowired
    private GenericRepository<T,U> repository;

//    @Cacheable(cacheNames = "auth-service-cache")
    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

//    @Cacheable(cacheNames = "auth-service-cache")
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

//    @CacheEvict(cacheNames = "auth-service-cache")
    @Override
    public void delete(T entity) {
        repository.delete(entity);
    }

//    @CacheEvict(cacheNames = "auth-service-cache")
    @Override
    public void deleteById(U id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);}
    }

}
