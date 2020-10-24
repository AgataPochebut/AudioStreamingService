package com.it.conversionservice.service;

import java.io.File;

public interface StorageService {

    File upload(org.springframework.core.io.Resource source, String name) throws Exception;

    org.springframework.core.io.Resource download(File resource) throws Exception;

    void delete(File resource) throws Exception;

    boolean exist(File resource);

}
