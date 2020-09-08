package com.it.playservice.service.repository;

import java.util.List;

public interface GenericRepositoryService<T,U> {

    List<T> findAll() throws Exception;

    T findById(U id) throws Exception;

    T save(T entity) throws Exception;

    T update(T entity) throws Exception;

    void deleteById(U id) throws Exception;

    void delete(T entity) throws Exception;

}
