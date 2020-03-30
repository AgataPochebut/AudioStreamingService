package com.epam.service.repository;

import com.epam.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;

import java.util.List;

public class GenericServiceImpl<T,U> implements GenericService<T, U> {

    @Autowired
    private GenericRepository<T,U> repository;

    @Override
    public List<T> findAll() {
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
        repository.deleteById(id);
    }

    @Override
    public void delete(T entity) { repository.delete(entity); }
}
