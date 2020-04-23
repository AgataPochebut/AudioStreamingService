package com.epam.conversionservice.service;

import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

public interface ConversionService {

    File convert(File file, String format) throws IOException;

    Resource convert(Resource source, String format) throws IOException;
}
