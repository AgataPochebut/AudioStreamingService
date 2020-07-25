package com.epam.playservice.service.repository;

import com.epam.playservice.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GenericRepositoryServiceImpl<T,U> implements GenericRepositoryService<T, U> {

    @Autowired
    private GenericRepository<T,U> repository;

    @Override
    public List<T> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public T findById(U id) throws Exception {
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
    public void delete(T entity) throws Exception {
        repository.delete(entity);
    }

    @Override
    public void deleteById(U id) throws Exception {
        if (repository.existsById(id)) {
            repository.deleteById(id);}
    }

}
