package com.epam.conversionservice.service;

import java.io.File;
import java.io.IOException;

public interface ConversionService {

    File convert(File file, String format) throws IOException;

}
