package com.epam.service.search;

public interface GenericSearchService<T,U> {

    Iterable<T> findAll();

    T findById(U id);

    T save(T entity) throws Exception;

    T update(T entity) throws Exception;

    void deleteById(U id);

    void delete(T entity);

}
