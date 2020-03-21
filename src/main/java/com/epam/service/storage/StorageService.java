package com.epam.service.storage;

import org.springframework.core.io.Resource;

import java.util.List;

public interface StorageService {

    Resource findById(String path);

    Resource save(Resource file) throws Exception;

    void deleteById(String path);

    boolean checkIfExistById(String path);
}
