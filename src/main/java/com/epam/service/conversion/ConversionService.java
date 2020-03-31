package com.epam.service.conversion;

import java.io.File;
import java.io.IOException;

public interface ConversionService {

    File convert(File file, String format) throws IOException;
}
