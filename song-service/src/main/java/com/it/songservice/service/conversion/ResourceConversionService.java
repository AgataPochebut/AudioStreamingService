package com.it.songservice.service.conversion;

import com.it.songservice.model.Resource;

public interface ResourceConversionService {

   Resource convert(Resource entity, String format) throws Exception;

}
