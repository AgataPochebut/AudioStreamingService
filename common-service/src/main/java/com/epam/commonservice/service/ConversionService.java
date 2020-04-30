package com.epam.commonservice.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public interface ConversionService {

    File convert(File file, String format) throws IOException;

    Resource convert(Resource source, String format) throws IOException;

    String test(String name);
}
