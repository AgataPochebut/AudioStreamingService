package com.it.searchservice.service;

import java.util.List;

public interface GenericRepositoryService<T,U> {

    List<T> findAll();

    T findById(U id);

    T save(T entity) throws Exception;

    T update(T entity) throws Exception;

    void deleteById(U id);

    List<T> search(String keyword);

}
