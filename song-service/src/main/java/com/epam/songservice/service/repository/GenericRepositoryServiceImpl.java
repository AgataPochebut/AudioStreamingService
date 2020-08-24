package com.epam.songservice.service.repository;

import com.epam.songservice.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GenericRepositoryServiceImpl<T,U> implements GenericRepositoryService<T, U> {

    @Autowired
    private GenericRepository<T,U> repository;

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findById(U id) {
        return repository.findById(id).orElse(null);
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
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteById(U id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);}
    }
}
