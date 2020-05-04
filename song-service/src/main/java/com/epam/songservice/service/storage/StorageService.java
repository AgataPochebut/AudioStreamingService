package com.epam.songservice.service.storage;

public interface StorageService<T,U> {

    org.springframework.core.io.Resource download(T entity) throws Exception;

//    org.springframework.core.io.Resource download(Long id);

    T upload(org.springframework.core.io.Resource source, String name) throws Exception;

    void delete(T entity);

//    void delete(Long id);

    boolean exist(T entity);

//    boolean exist(Long id);

    String test();
}
