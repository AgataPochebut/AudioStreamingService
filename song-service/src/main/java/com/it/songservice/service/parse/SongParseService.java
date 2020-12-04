package com.it.songservice.service.parse;

import com.it.songservice.model.Resource;
import com.it.songservice.model.Song;

public interface SongParseService {

   Song parse(Resource resource) throws Exception;

}
