package com.it.conversionservice.service;

import java.io.File;

public interface ConversionService {

    File convert(File file, String format) throws Exception;

}
