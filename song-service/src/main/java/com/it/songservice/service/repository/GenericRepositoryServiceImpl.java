package com.it.songservice.service.repository;

import com.it.songservice.repository.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;

@Transactional
public abstract class GenericRepositoryServiceImpl<T, U> implements GenericRepositoryService<T, U> {

    @Autowired
    private EntityManager em;

    @Autowired
    private GenericRepository<T, U> repository;

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T findById(U id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entity with this id not exist"));
    }

    @Override
    public T save(T entity) throws Exception {
        return em.merge(entity);
    }

    @Override
    public T update(T entity) throws Exception {
        return repository.save(entity);
    }

    @Override
    public void deleteById(U id) {
        if(existById(id)) repository.deleteById(id);
        else throw new EntityNotFoundException("Entity with this id not exist");
    }

    @Override
    public boolean existById(U id) {
        return repository.existsById(id);
    }
}
