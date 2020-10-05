package com.it.songservice.service.repository;

import com.it.songservice.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
public abstract class GenericRepositoryServiceImpl<T,U> implements GenericRepositoryService<T, U> {

    @Autowired
    private EntityManager em;

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
//        return repository.save(entity);
        return em.merge(entity);
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
    public boolean existById(U id) {
        return repository.existsById(id);
    }
}
