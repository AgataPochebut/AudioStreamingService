package com.epam.authservice.service.repository;

import com.epam.authservice.repository.GenericRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class GenericRepositoryServiceImpl<T,U> implements GenericRepositoryService<T, U> {

    private final GenericRepository<T,U> repository;

    public GenericRepositoryServiceImpl(GenericRepository<T,U> repository) {
        this.repository = repository;
    }

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
    public void delete(T entity) {
        repository.delete(entity);
    }

    @Override
    public void deleteById(U id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);}
    }

}
