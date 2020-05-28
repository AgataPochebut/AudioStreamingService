package com.epam.playservice.service;

import java.util.List;

public interface GenericService<T,U> {

    List<T> findAll();

    T findById(U id);

    T save(T entity) throws Exception;

    T update(T entity) throws Exception;

    void deleteById(U id);

    void delete(T entity);

}