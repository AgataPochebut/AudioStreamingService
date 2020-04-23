package com.epam.audiostreamingservice.service.conversion;

import java.io.File;
import java.io.IOException;
import org.springframework.core.io.Resource;

public interface ConversionService {

    File convert(File file, String format) throws IOException;

    Resource convert(Resource source, String format) throws IOException;
}
